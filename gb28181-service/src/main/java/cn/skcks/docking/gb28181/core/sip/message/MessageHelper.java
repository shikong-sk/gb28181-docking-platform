package cn.skcks.docking.gb28181.core.sip.message;

import lombok.SneakyThrows;

import javax.sip.SipFactory;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;

public class MessageHelper {
    public static SipFactory getSipFactory() {
        return SipFactory.getInstance();
    }

    @SneakyThrows
    public static AddressFactory createAddressFactory() {
        return getSipFactory().createAddressFactory();
    }

    @SneakyThrows
    public static HeaderFactory createHeaderFactory() {
        return getSipFactory().createHeaderFactory();
    }

    @SneakyThrows
    public static SipURI createSipURI(String id, String address) {
        return createAddressFactory().createSipURI(id, address);
    }

    @SneakyThrows
    public static Address createAddress(SipURI uri) {
        return createAddressFactory().createAddress(uri);
    }

    @SneakyThrows
    public static FromHeader createFromHeader(Address fromAddress, String fromTag) {
        return createHeaderFactory().createFromHeader(fromAddress, fromTag);
    }

    @SneakyThrows
    public static ToHeader createToHeader(Address toAddress, String toTag) {
        return createHeaderFactory().createToHeader(toAddress, toTag);
    }

    @SneakyThrows
    public static MaxForwardsHeader createMaxForwardsHeader(int maxForwards) {
        return createHeaderFactory().createMaxForwardsHeader(maxForwards);
    }
}
