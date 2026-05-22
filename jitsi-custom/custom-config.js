// Force BOSH (HTTP long-polling) instead of WebSocket to avoid wss:// protocol issues
// This file is mounted into jitsi-web container at /config/custom-config.js
config.websocket = null;
config.bosh = '/http-bind';
config.useTurnUdp = false;
config.prejoinPageEnabled = false;
config.prejoinConfig = config.prejoinConfig || {};
config.prejoinConfig.enabled = false;
config.enableWelcomePage = false;
config.requireDisplayName = false;
config.readOnlyName = true;
config.enableInsecureRoomNameWarning = false;
config.disableModeratorIndicator = true;
config.enableNoisyMicDetection = false;
config.startWithAudioMuted = true;
config.startWithVideoMuted = false;
config.p2p = config.p2p || {};
config.p2p.enabled = true;
