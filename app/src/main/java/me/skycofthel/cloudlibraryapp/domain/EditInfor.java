package me.skycofthel.cloudlibraryapp.domain;

import com.google.gson.annotations.SerializedName;

public class EditInfor {
    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("id")
        private Integer id;
        @SerializedName("name")
        private String name;
        @SerializedName("isbn")
        private String isbn;
        @SerializedName("press")
        private String press;
        @SerializedName("author")
        private String author;
        @SerializedName("pagination")
        private String pagination;
        @SerializedName("price")
        private String price;
        @SerializedName("uploadTime")
        private String uploadTime;
        @SerializedName("status")
        private String status;
        @SerializedName("borrower")
        private Object borrower;
        @SerializedName("borrowTime")
        private Object borrowTime;
        @SerializedName("returnTime")
        private Object returnTime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getPress() {
            return press;
        }

        public void setPress(String press) {
            this.press = press;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPagination() {
            return pagination;
        }

        public void setPagination(String pagination) {
            this.pagination = pagination;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime) {
            this.uploadTime = uploadTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getBorrower() {
            return borrower;
        }

        public void setBorrower(Object borrower) {
            this.borrower = borrower;
        }

        public Object getBorrowTime() {
            return borrowTime;
        }

        public void setBorrowTime(Object borrowTime) {
            this.borrowTime = borrowTime;
        }

        public Object getReturnTime() {
            return returnTime;
        }

        public void setReturnTime(Object returnTime) {
            this.returnTime = returnTime;
        }
    }
}
