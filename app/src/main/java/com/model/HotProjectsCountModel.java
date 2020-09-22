package com.model;

/**
 * Created by Naresh on 21-Dec-16.
 */

public class HotProjectsCountModel {
    private boolean success;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private int isHotProjectAvailable;
        private int isLiveProjectAvailable;
        private int isReadyToMoveInProjectAvailable;
        private int isLuxuryProjectAvailable;
        private int isAffordableHomesProjectAvailable;

        public int getIsHotProjectAvailable() {
            return isHotProjectAvailable;
        }

        public void setIsHotProjectAvailable(int isHotProjectAvailable) {
            this.isHotProjectAvailable = isHotProjectAvailable;
        }

        public int getIsLiveProjectAvailable() {
            return isLiveProjectAvailable;
        }

        public void setIsLiveProjectAvailable(int isLiveProjectAvailable) {
            this.isLiveProjectAvailable = isLiveProjectAvailable;
        }

        public int getIsReadyToMoveInProjectAvailable() {
            return isReadyToMoveInProjectAvailable;
        }

        public void setIsReadyToMoveInProjectAvailable(int isReadyToMoveInProjectAvailable) {
            this.isReadyToMoveInProjectAvailable = isReadyToMoveInProjectAvailable;
        }

        public int getIsLuxuryProjectAvailable() {
            return isLuxuryProjectAvailable;
        }

        public void setIsLuxuryProjectAvailable(int isLuxuryProjectAvailable) {
            this.isLuxuryProjectAvailable = isLuxuryProjectAvailable;
        }

        public int getIsAffordableHomesProjectAvailable() {
            return isAffordableHomesProjectAvailable;
        }

        public void setIsAffordableHomesProjectAvailable(int isAffordableHomesProjectAvailable) {
            this.isAffordableHomesProjectAvailable = isAffordableHomesProjectAvailable;
        }
    }
}
