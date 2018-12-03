package com.example.demo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ApiModel(description = "Represents an user of the system")
public class User {

    @ApiModelProperty(value = "The ID of the user", required = true)
    private Integer id;


    @ApiModelProperty(value = "The name of the user", required = true)
    @NotNull
    @Size(min = 5, max = 10, groups = OptionalChecks.class)
    private String name;
}
