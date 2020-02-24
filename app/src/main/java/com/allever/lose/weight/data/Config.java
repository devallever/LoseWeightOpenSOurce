package com.allever.lose.weight.data;

import org.litepal.crud.DataSupport;

/**
 * Created by Mac on 18/3/19.
 */

public class Config extends DataSupport {
    public static final int LANG_CHINESE = 1;
    public static final int LANG_ENGLISH = 2;

    private int id;

    //静音选项
    private boolean muteOption;
    //语音提示
    private boolean voiceOption;
    //训练提示
    private boolean trainVoiceOption;
    //语言
    private int language;

    private boolean sync;
    private String account;
    private long syncTime;

    //TTS引擎 packageName
    private String tts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isMuteOption() {
        return muteOption;
    }

    public void setMuteOption(boolean muteOption) {
        this.muteOption = muteOption;
    }

    public boolean isVoiceOption() {
        return voiceOption;
    }

    public void setVoiceOption(boolean voiceOption) {
        this.voiceOption = voiceOption;
    }

    public boolean isTrainVoiceOption() {
        return trainVoiceOption;
    }

    public void setTrainVoiceOption(boolean trainVoiceOption) {
        this.trainVoiceOption = trainVoiceOption;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(long syncTime) {
        this.syncTime = syncTime;
    }

    public static class Reminder extends DataSupport {
        private int id;
        private int hour;
        private int minute;
        private int second;
        private boolean remindSwitch;
        private boolean monRepeat;
        private boolean tueRepeat;
        private boolean webRepeat;
        private boolean thurRepeat;
        private boolean friRepeat;
        private boolean satRepeat;
        private boolean sunRepeat;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public boolean isRemindSwitch() {
            return remindSwitch;
        }

        public void setRemindSwitch(boolean remindSwitch) {
            this.remindSwitch = remindSwitch;
        }

        public boolean isMonRepeat() {
            return monRepeat;
        }

        public void setMonRepeat(boolean monRepeat) {
            this.monRepeat = monRepeat;
        }

        public boolean isTueRepeat() {
            return tueRepeat;
        }

        public void setTueRepeat(boolean tueRepeat) {
            this.tueRepeat = tueRepeat;
        }

        public boolean isWebRepeat() {
            return webRepeat;
        }

        public void setWebRepeat(boolean webRepeat) {
            this.webRepeat = webRepeat;
        }

        public boolean isThurRepeat() {
            return thurRepeat;
        }

        public void setThurRepeat(boolean thurRepeat) {
            this.thurRepeat = thurRepeat;
        }

        public boolean isFriRepeat() {
            return friRepeat;
        }

        public void setFriRepeat(boolean friRepeat) {
            this.friRepeat = friRepeat;
        }

        public boolean isSatRepeat() {
            return satRepeat;
        }

        public void setSatRepeat(boolean satRepeat) {
            this.satRepeat = satRepeat;
        }

        public boolean isSunRepeat() {
            return sunRepeat;
        }

        public void setSunRepeat(boolean sunRepeat) {
            this.sunRepeat = sunRepeat;
        }
    }
}
