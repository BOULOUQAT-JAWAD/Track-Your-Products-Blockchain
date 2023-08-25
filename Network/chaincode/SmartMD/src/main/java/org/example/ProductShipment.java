package org.example;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;

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


}
