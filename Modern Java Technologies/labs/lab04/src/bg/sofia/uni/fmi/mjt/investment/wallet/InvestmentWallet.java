package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.SimpleAcquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.InsufficientResourcesException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.OfferPriceException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.UnknownAssetException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.Quote;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;

import java.time.LocalDateTime;
import java.util.*;

public class InvestmentWallet implements Wallet {

    private final QuoteService quoteService;
    private final List<Acquisition> acquisitions = new ArrayList<>();
    private final Map<Asset, Integer> assetToQuantity = new HashMap<>();

    private double balance = 0.0;

    public InvestmentWallet(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    public double deposit(double cash) {
        requireNonNegative(cash);

        balance += cash;

        return balance;
    }

    private void requireNonNegative(double x) {
        if (x < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void requireNonNegative(int x) {
        if (x < 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public double withdraw(double cash) throws InsufficientResourcesException {
        requireNonNegative(cash);

        if (balance - cash < 0) {
            throw new InsufficientResourcesException();
        }

        balance -= cash;

        return balance;
    }

    @Override
    public Acquisition buy(Asset asset, int quantity, double maxPrice) throws WalletException {
        requireNonNull(asset);
        requireNonNegative(quantity);
        requireNonNegative(maxPrice);

        Quote quote = quoteService.getQuote(asset);

        validateBuyingAssetOrThrow(quote, maxPrice);

        Acquisition acquisition = createAcquisition(quote.askPrice(), LocalDateTime.now(), quantity, asset);
        acquisitions.add(acquisition);

        increaseAssetsQuantity(asset, quantity);

        return acquisition;
    }

    private Acquisition createAcquisition(double price, LocalDateTime timeStamp, int quantity, Asset asset) {
        return new SimpleAcquisition(price, timeStamp, quantity, asset);
    }

    private void increaseAssetsQuantity(Asset asset, int quantity) {
        int currentQuantity = assetToQuantity.getOrDefault(asset, 0);
        assetToQuantity.put(asset, currentQuantity + quantity);
    }

    private void validateBuyingAssetOrThrow(Quote quote, double maxPrice) throws WalletException {
        if (quote == null) {
            throw new UnknownAssetException();
        }

        if (quote.askPrice() > maxPrice) {
            throw new OfferPriceException();
        }

        if (quote.askPrice() > balance) {
            throw new InsufficientResourcesException();
        }
    }

    private void requireNonNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public double sell(Asset asset, int quantity, double minPrice) throws WalletException {
        requireNonNull(asset);
        requireNonNegative(quantity);
        requireNonNegative(minPrice);

        int assetsQuantity = assetToQuantity.getOrDefault(asset, 0);
        Quote quote = quoteService.getQuote(asset);

        validateSellingAssetOrThrow(quote, quantity, assetsQuantity, minPrice);

        if (assetsQuantity - quantity == 0) {
            assetToQuantity.remove(asset);
        } else {
            assetToQuantity.put(asset, assetsQuantity - quantity);
        }

        return quote.bidPrice() * quantity;
    }

    private void validateSellingAssetOrThrow(Quote quote, int quantity, int assetsQuantity, double minPrice)
        throws WalletException {
        if (assetsQuantity < quantity) {
            throw new InsufficientResourcesException();
        }

        if (quote == null) {
            throw new UnknownAssetException();
        }

        if (quote.bidPrice() < minPrice) {
            throw new OfferPriceException();
        }
    }

    @Override
    public double getValuation() {
        double valuation = 0.0;

        for (var kv : assetToQuantity.entrySet()) {
            Asset asset = kv.getKey();
            Quote quote = quoteService.getQuote(asset);
            int quantity =  kv.getValue();
            valuation += quote.bidPrice() * quantity;
        }

        return valuation;
    }

    @Override
    public double getValuation(Asset asset) throws UnknownAssetException {
        requireNonNull(asset);

        if (!assetToQuantity.containsKey(asset)) {
            throw new UnknownAssetException();
        }

        Quote quote = quoteService.getQuote(asset);

        if (quote == null) {
            throw new UnknownAssetException();
        }

        return quote.bidPrice() * assetToQuantity.get(asset);
    }

    @Override
    public Asset getMostValuableAsset() {
        Asset mostValuableAsset = null;
        double mostValuableAssetsValuation = 0.0;

        for (Asset asset : assetToQuantity.keySet()) {
            double currentValuation;
            try {
                currentValuation = getValuation(asset);
            } catch (UnknownAssetException e) {
                continue; /* This asset's value cannot be computed by the quote service at this instant */
            }

            if (currentValuation > mostValuableAssetsValuation) {
                mostValuableAsset = asset;
                mostValuableAssetsValuation = currentValuation;
            }
        }

        return mostValuableAsset;
    }

    @Override
    public Collection<Acquisition> getAllAcquisitions() {
        return List.copyOf(acquisitions);
    }

    @Override
    public Set<Acquisition> getLastNAcquisitions(int n) {
        requireNonNegative(n);

        if (n >= acquisitions.size()) {
            return Set.copyOf(acquisitions);
        }

        return Set.copyOf(acquisitions.subList(acquisitions.size() - n, acquisitions.size()));
    }
}
