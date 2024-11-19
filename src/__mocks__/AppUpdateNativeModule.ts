export default {
    getVersionCode: jest.fn(() => 111),
    downloadApp: jest.fn(),
    installApp: jest.fn(),

    addListener: jest.fn(),
    removeListeners: jest.fn(),
};
