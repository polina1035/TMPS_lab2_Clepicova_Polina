package factorymethod;

/** Concrete creator: long-distance delivery with car couriers. */
public class CarDelivery extends DeliveryService {
    @Override
    protected Courier createCourier() {
        return new CarCourier();
    }
}
