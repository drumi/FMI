package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

abstract class BaseAsset implements Asset {

    private final String id;
    private final String name;
    private final AssetType type;

    public BaseAsset(String id, String name, AssetType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AssetType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseAsset baseAsset = (BaseAsset) o;

        return id.equals(baseAsset.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
