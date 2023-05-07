package com.example.etrend_kovetes;

public class Etel {
        private String id;
        private String name;
        private String amount;


        private String etelId;
        private String amounttype;
        private long createDate;

        public Etel() {}
        public Etel(String id, String name, String amount, String etelId, String amounttype , long timestamp){
                this.name=name;
                this.amount=amount;
                this.etelId = etelId;
                this.createDate=timestamp;
                this.id=id;
                this.amounttype=amounttype;
        }


        public String getName() {
                return name;
        }
        public void setName(String name) {
                this.name = name;
        }

        public String getId() {
                return id;
        }
        public void setId(String id) {
                this.id = id;
        }

        public void setAmounttype(String amounttype) {
                this.amounttype = amounttype;
        }
        public String getAmounttype() {
                return amounttype;
        }


        public String getAmount() {
                return amount;
        }

        public void setAmount(String amount) {
                this.amount = amount;
        }

        public long getCreateDate() {
                return createDate;
        }

        public void setCreateDate(long timestamp) {
                this.createDate = timestamp;
        }

        public String getEtelId() {
                return etelId;
        }
        public void setEtelId(String etelId) {
                this.etelId = etelId;
        }

}
