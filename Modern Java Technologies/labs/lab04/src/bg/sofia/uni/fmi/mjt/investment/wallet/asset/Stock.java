package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Stock extends BaseAsset {

    private static final AssetType ASSET_TYPE = AssetType.STOCK;

    public Stock(String id, String name) {
        super(id, name, ASSET_TYPE);
    }

}
