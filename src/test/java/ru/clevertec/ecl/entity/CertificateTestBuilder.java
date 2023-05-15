package ru.clevertec.ecl.entity;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CertificateTestBuilder {
    private long id = 0;
    private String name = null;
    private String description = null;
    private int price = 0;
    private int duration = 0;
    private ZonedDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<Tag> tags = new ArrayList<>();


    private CertificateTestBuilder() {
    }

    private CertificateTestBuilder(CertificateTestBuilder cert) {
        this.id = cert.id;
        this.name = cert.name;
        this.description = cert.description;
        this.price = cert.price;
        this.duration = cert.duration;
        this.createDate = cert.createDate;
        this.lastUpdateDate = cert.lastUpdateDate;
        this.tags = cert.tags;
    }

    public static CertificateTestBuilder aCertificate() {
        return new CertificateTestBuilder();
    }

    public CertificateTestBuilder addTag(Tag tag) {
        tags.add(tag);
        return this;
    }

    public CertificateTestBuilder id(long id) {
        return copyWith(c -> c.id = id);
    }

    public CertificateTestBuilder name(String name) {
        return copyWith(c -> c.name = name);
    }

    public CertificateTestBuilder description(String description) {
        return copyWith(c -> c.description = description);
    }

    public CertificateTestBuilder price(int price) {
        return copyWith(c -> c.price = price);
    }

    public CertificateTestBuilder duration(int duration) {
        return copyWith(c -> c.duration = duration);
    }

    public CertificateTestBuilder lastUpdateDate(LocalDateTime lastUpdateDate) {
        return copyWith(c -> c.lastUpdateDate = lastUpdateDate);
    }

    private CertificateTestBuilder copyWith(Consumer<CertificateTestBuilder> consumer) {
        final CertificateTestBuilder copy = new CertificateTestBuilder(this);
        consumer.accept(copy);
        return copy;
    }

    public GiftCertificate build() {
        GiftCertificate cert = new GiftCertificate(id, name, description, price, duration, createDate, lastUpdateDate);
        if (tags.size() > 0) cert.setTags(tags);
        return cert;
    }
}
