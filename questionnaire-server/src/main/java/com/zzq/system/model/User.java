package com.zzq.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zzq.common.validate.groups.Create;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sherlock
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = -3200103254689137288L;

    private Integer userId;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @JsonIgnore
    @NotBlank(message = "密码不能为空", groups = Create.class)
    private String password;

    @JsonIgnore
    private String salt;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String status;

    private Date lastLoginTime;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date modifyTime;

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", salt='" + salt + '\'' +
            ", email='" + email + '\'' +
            ", status='" + status + '\'' +
            ", lastLoginTime=" + lastLoginTime +
            ", createTime=" + createTime +
            ", modifyTime=" + modifyTime +
            '}';
    }
}