package com.example.demo.web.app.mod1;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Mod1Form implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(groups = { Update.class, Delete.class })
    @Size(max = 0, groups = { Create.class })
    private String id;

    @Size(max = 10, groups = { Create.class, Update.class })
    private String prop1;

    @Size(max = 10, groups = { Create.class, Update.class })
    private String prop2;

    @Size(max = 10, groups = { Create.class, Update.class })
    private String prop3;

    @Max(value = 0, groups = { Create.class })
    @Min(value = 1, groups = { Update.class })
    private long version;

    public interface Create {
    }

    public interface Update {
    }

    public interface Delete {
    }
}
