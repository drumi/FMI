package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Gold extends BaseAsset {

    private static final AssetType ASSET_TYPE = AssetType.GOLD;

    public Gold(String id, String name) {
        super(id, name, ASSET_TYPE);
    }

}
