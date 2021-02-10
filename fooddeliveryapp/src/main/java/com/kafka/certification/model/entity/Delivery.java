package com.kafka.certification.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"deliveryUser", "products"})
@EqualsAndHashCode(exclude =  {"deliveryUser", "products" })
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "billing_doc_name")
    private byte [] billingDocName;

    @NotNull
    @Column(name = "delivery_serial")
    private byte [] deliverySerial;

    @NotNull
    @Column(name = "billing_doc_num")
    private byte [] deliveryNum;

    @NotNull
    @Column(name = "restaurant_name")
    private byte [] restaurantName;


    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;


    @NotNull
    @OneToOne
    @JoinColumn(
            name = "delivery_user_id",
            referencedColumnName = "id"
    )
    private User deliveryUser;

    @OneToMany(mappedBy = "delivery")
    private List<Product> products;



}
