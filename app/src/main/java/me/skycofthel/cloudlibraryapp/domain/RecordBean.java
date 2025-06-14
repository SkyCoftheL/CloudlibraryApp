package me.skycofthel.cloudlibraryapp.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecordBean {

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
            @SerializedName("bookname")
            private String bookname;
            @SerializedName("bookisbn")
            private String bookisbn;
            @SerializedName("borrower")
            private String borrower;
            @SerializedName("borrowTime")
            private String borrowTime;
            @SerializedName("remandTime")
            private String remandTime;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getBookname() {
                return bookname;
            }

            public void setBookname(String bookname) {
                this.bookname = bookname;
            }

            public String getBookisbn() {
                return bookisbn;
            }

            public void setBookisbn(String bookisbn) {
                this.bookisbn = bookisbn;
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

            public String getRemandTime() {
                return remandTime;
            }

            public void setRemandTime(String remandTime) {
                this.remandTime = remandTime;
            }
        }
    }
}
