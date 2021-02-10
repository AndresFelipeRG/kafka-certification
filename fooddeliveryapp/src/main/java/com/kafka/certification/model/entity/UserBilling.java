package com.kafka.certification.model.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_deliveries")
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"billingDocumentImage", "deliveries"})
@EqualsAndHashCode(exclude =  {"billingDocumentImage", "deliveries" })
public class UserBilling {


}
