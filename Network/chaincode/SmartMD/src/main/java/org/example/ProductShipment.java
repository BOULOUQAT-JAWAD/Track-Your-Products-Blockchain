package org.example;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.List;

@Contract(
        name = "Shipment process",
        info = @Info(
                title = "Product shipment contract",
                description = "A Smart Contract for the shipment process",
                version = "0.0.1-SNAPSHOT"))
@Default()
public class ProductShipment implements ContractInterface {

    private final Genson genson = new Genson();

    private enum PackageErrors {
        PACKAGE_NOT_FOUND,
        PACKAGE_ALREADY_EXISTS
    }

    /*
    Context, which represents the context of the transaction.
    It contains information about the transaction's execution environment
    and provides access to the chaincode stub.
    */
    /*
    The chaincode stub is an interface that allows the smart contract code to interact with the blockchain ledger,
    read and write state data, and invoke other chaincode functions.
     */

    @Transaction()
    public Package createPackage(final Context ctx, final String id, final List<Product> products,
                                 final String from,final String to) {
        ChaincodeStub stub = ctx.getStub();

        String packageState = stub.getStringState(id);
        if (!packageState.isEmpty()) {
            String errorMessage = String.format("Package %s already exists", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PackageErrors.PACKAGE_ALREADY_EXISTS.toString());
        }

        Package aPackage = new Package(id,products,from,to,from,false);
        packageState = genson.serialize(aPackage);
        stub.putStringState(id, packageState);

        return aPackage;
    }

    @Transaction()
    public Package changePackageLocation(final Context ctx, final String id, final String current_location) {
        ChaincodeStub stub = ctx.getStub();

        String packageState = stub.getStringState(id);

        if (packageState.isEmpty()) {
            String errorMessage = String.format("Package %s does not exist", id);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PackageErrors.PACKAGE_NOT_FOUND.toString());
        }

        Package aPackage = genson.deserialize(packageState, Package.class);

        boolean delivered = aPackage.getTo().matches(current_location);

        Package newPackage = new Package(id,aPackage.getProducts(),aPackage.getFrom(),aPackage.getTo(),current_location,delivered);
        String newPackageState = genson.serialize(newPackage);
        stub.putStringState(id, newPackageState);

        return newPackage;
    }
}
