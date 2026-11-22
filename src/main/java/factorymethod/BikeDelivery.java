package factorymethod;

/** Concrete creator: short-distance delivery with bicycle couriers. */
public class BikeDelivery extends DeliveryService {
    @Override
    protected Courier createCourier() {
        return new BikeCourier();
    }
}
