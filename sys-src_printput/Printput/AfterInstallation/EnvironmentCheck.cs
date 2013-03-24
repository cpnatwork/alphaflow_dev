using System;


namespace InstallationOverride
{
    class EnvironmentCheck
    {
        private static bool Is64BitProcess
        {
            get { return IntPtr.Size == 8; }
        }

        public static bool Is64BitOperatingSystem
        {
            get
            {
                // Clearly if this is a 64-bit process we must be on a 64-bit OS.
                if (Is64BitProcess)
                    return true;

                // Ok, so we are a 32-bit process, but is the OS 64-bit?
                // If we are running under Wow64 than the OS is 64-bit.
                bool isWow64;
                return ModuleContainsFunction("kernel32.dll", "IsWow64Process") && EnvironmentCheckPinvoke.IsWow64Process(EnvironmentCheckPinvoke.GetCurrentProcess(), out isWow64) && isWow64;
            }
        }

        private static bool ModuleContainsFunction(string moduleName, string methodName)
        {
            IntPtr hModule = EnvironmentCheckPinvoke.GetModuleHandle(moduleName);
            if (hModule != IntPtr.Zero)
                return EnvironmentCheckPinvoke.GetProcAddress(hModule, methodName) != IntPtr.Zero;

            throw new Exception("GetModuleHandle failed!");
        }
    }
}
