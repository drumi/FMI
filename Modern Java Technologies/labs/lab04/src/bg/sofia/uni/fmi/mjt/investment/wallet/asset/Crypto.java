package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Crypto extends BaseAsset {

    private static final AssetType ASSET_TYPE = AssetType.CRYPTO;

    public Crypto(String id, String name) {
        super(id, name, ASSET_TYPE);
    }

}
