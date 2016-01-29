# cordova-intermec-scanner
Cordova/Phonegap plugin that consumes the Intermec Barcode Scanner API

This plugin allows a developer to hook into an Android Intermec devices datacollector service.

# Install

<code>cordova plugin add cordova-intermec-scanner</code>

# Enable Scanner
<code>
intermecScanner.activateScanner(function (result) {
          console.log("Scanner Result : " + result);
        });
</code>

# Disable Scanner
<code>
intermecScanner.deactivateScanner(function () {
          _log.d("Scanner Deactivated...");
        });
</code>
