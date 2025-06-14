package me.skycofthel.cloudlibraryapp.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectNewBooks {
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
        @SerializedName("total")
        private Integer total;
        @SerializedName("rows")
        private List<RowsDTO> rows;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<RowsDTO> getRows() {
            return rows;
        }

        public void setRows(List<RowsDTO> rows) {
            this.rows = rows;
        }

        public static class RowsDTO {
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
            private Integer pagination;
            @SerializedName("price")
            private Integer price;
            @SerializedName("uploadTime")
            private String uploadTime;
            @SerializedName("status")
            private String status;
            @SerializedName("borrower")
            private String borrower;
            @SerializedName("borrowTime")
            private String borrowTime;
            @SerializedName("returnTime")
            private String returnTime;

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

            public Integer getPagination() {
                return pagination;
            }

            public void setPagination(Integer pagination) {
                this.pagination = pagination;
            }

            public Integer getPrice() {
                return price;
            }

            public void setPrice(Integer price) {
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

            public String getBorrower() {
                return borrower;
            }

            public void setBorrower(String borrower) {
                this.borrower = borrower;
            }

            public String getBorrowTime() {
                return borrowTime;
            }

            public void setBorrowTime(String borrowTime) {
                this.borrowTime = borrowTime;
            }

            public String getReturnTime() {
                return returnTime;
            }

            public void setReturnTime(String returnTime) {
                this.returnTime = returnTime;
            }
        }
    }
}
