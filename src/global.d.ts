declare var global: typeof globalThis & {
    __turboModuleProxy: <T extends TurboModule>(
        turboModuleName: string,
    ) => T | null;
};
