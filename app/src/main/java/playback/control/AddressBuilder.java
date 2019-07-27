package playback.control;

class AddressBuilder {

    private int port = 44321;
    private int lastIpPartDefault = 3;
    private int lastIpPart = 3;
    private boolean isError = false;
    private boolean shouldIncreaseIpSegment = false;


    void onError() {
        isError = true;
        if (lastIpPart == 1) {
            shouldIncreaseIpSegment = true;
        }
    }

    void onSuccess() {
        isError = false;
    }

    String getNextUrl() {

        if (isError) {
            if (!shouldIncreaseIpSegment) {
                lastIpPart--;
            } else {
                if (lastIpPart <= 254) {
                    lastIpPart++;
                } else {
                    lastIpPart = lastIpPartDefault;
                }
            }
        }
        return "http://192.168.0." + lastIpPart + ":" + port;
    }

    public void reset() {
        isError = false;
        shouldIncreaseIpSegment = false;
        lastIpPart = lastIpPartDefault;
    }
}
