package com.nupday.bo;

public class DeleteObjectBo {
    private Integer id;
    private Boolean toDustbin;

    public DeleteObjectBo() {
    }

    public DeleteObjectBo(Integer id, Boolean toDustbin) {
        this.id = id;
        this.toDustbin = toDustbin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getToDustbin() {
        return toDustbin;
    }

    public void setToDustbin(Boolean toDustbin) {
        this.toDustbin = toDustbin;
    }
}
