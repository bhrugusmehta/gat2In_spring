package com.gat2in.ordersystem.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RoleDAO {
    List<String> strRoles = new ArrayList<>();
}
