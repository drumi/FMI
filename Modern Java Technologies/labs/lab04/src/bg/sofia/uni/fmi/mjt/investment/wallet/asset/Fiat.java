package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Fiat extends BaseAsset {

    private static final AssetType ASSET_TYPE = AssetType.FIAT;

    public Fiat(String id, String name) {
        super(id, name, ASSET_TYPE);
    }

}
