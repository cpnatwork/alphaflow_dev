using System;
using System.Text;
using System.IO;
using System.IO.Pipes;

namespace Printput
{    
        public class NamedPipeThreadClass
        {
            //-> Registry
            private const int BufferSize = 65536;

            private readonly string _globalUniqueId;
            private readonly string _pipedFile;

            private static void WriteLog(String line)
            {
                StreamWriter sw = new StreamWriter(Environment.GetEnvironmentVariable("TEMP") + "\\mainlog.txt", true);

                sw.Write(line + Environment.NewLine);
                sw.Close();
            }

            public NamedPipeThreadClass(String globalUniqueId, String pipedFile)
            {
                _globalUniqueId = globalUniqueId;
                _pipedFile = pipedFile;
            }

            public void Start()
            {
                CreateNamedPipe(_globalUniqueId, _pipedFile);
            }


            /// <summary>
            /// Use the pipe classes in the System.IO.Pipes namespace to create the 
            /// named pipe. This solution is recommended.
            /// </summary>
            public static void CreateNamedPipe(String guidPipeName, String pipedFile)
            {
                NamedPipeServerStream pipeServer = null;

                try
                {
                    // Prepare the security attributes (the pipeSecurity parameter in 
                    // the constructor of NamedPipeServerStream) for the pipe. This 
                    // is optional. If pipeSecurity of NamedPipeServerStream is null, 
                    // the named pipe gets a default security descriptor.and the 
                    // handle cannot be inherited. The ACLs in the default security 
                    // descriptor of a pipe grant full control to the LocalSystem 
                    // account, (elevated) administrators, and the creator owner. 
                    // They also give only read access to members of the Everyone 
                    // group and the anonymous account. However, if you want to 
                    // customize the security permission of the pipe, (e.g. to allow 
                    // Authenticated Users to read from and write to the pipe), you 
                    // need to create a PipeSecurity object.
                    WriteLog("Getting pipeSecurity attributes");
                    PipeSecurity pipeSecurity = CreateSystemIOPipeSecurity();
                    if (pipeSecurity != null)
                        WriteLog("Getting pipeSecurity attributes successfully");
                    else WriteLog("Getting pipeSecurity attributes failed!");

                    WriteLog("Creating the named pipe.");
                    // Create the named pipe.
                    pipeServer = new NamedPipeServerStream(
                        guidPipeName,               // The unique pipe name.
                        PipeDirection.InOut,            // The pipe is duplex
                        //NamedPipeServerStream.MaxAllowedServerInstances,
                        1,                              // Wir erwarten nur einen Client!
                        PipeTransmissionMode.Message,   // Message-based communication
                        PipeOptions.Asynchronous,       // None for no additional parameters
                        BufferSize,             // Input buffer size
                        BufferSize,             // Output buffer size
                        pipeSecurity,                   // Pipe security attributes
                        HandleInheritability.None       // Not inheritable
                        );
                    WriteLog("Created the named pipe.");



                    WriteLog("Waiting for connection");
                    // Wait 10 seconds for the client to connect.
                    IAsyncResult asyncResult = pipeServer.BeginWaitForConnection(null, null);
                    //timeout -> settings
                    if (asyncResult.AsyncWaitHandle.WaitOne(10000))
                    {
                        WriteLog("Got signal");
                        pipeServer.EndWaitForConnection(asyncResult);
                        // success                   
                    }

                    //kommentieren

                    if (pipeServer.IsConnected == false)
                    {
                        //timeout...                    
                        WriteLog("Timeout");
                        return;
                    }


                    WriteLog("Sending file!");
                    // 
                    // Send file from server to client.
                    // 
                    String message = pipedFile;
                    byte[] bResponse = Encoding.Unicode.GetBytes(message);
                    int cbResponse = bResponse.Length;

                    pipeServer.Write(bResponse, 0, cbResponse);


                    //Flush the pipe to allow the client to read the pipe's contents 
                    //before disconnecting. Then disconnect the client's connection.
                    pipeServer.WaitForPipeDrain();
                    pipeServer.Disconnect();

                    WriteLog("File transferred succesfully!");
                }
                catch (Exception ex)
                {
                    WriteLog(ex.Message);
                }
                finally
                {
                    if (pipeServer != null)
                    {
                        pipeServer.Close();                        
                    }
                }
            }


            /// <summary>
            /// The CreateSystemIOPipeSecurity function creates a new PipeSecurity 
            /// object to allow All Users read and write access to a pipe.
            /// </summary>
            /// <returns>
            /// A PipeSecurity object that allows All Users read and write access to a pipe
            /// </returns>
            /// <see cref="http://msdn.microsoft.com/en-us/library/aa365600(VS.85).aspx"/>
            static PipeSecurity CreateSystemIOPipeSecurity()
            {
                try
                {
                    PipeSecurity pipeSecurity = new PipeSecurity();
                    
                    System.Security.Principal.SecurityIdentifier sid = new System.Security.Principal.SecurityIdentifier(System.Security.Principal.WellKnownSidType.WorldSid, null);
                    PipeAccessRule psRule = new PipeAccessRule(sid, PipeAccessRights.ReadWrite, System.Security.AccessControl.AccessControlType.Allow);
                    pipeSecurity.AddAccessRule(psRule);
                    return pipeSecurity;
                }
                catch (Exception ex)
                {
                    WriteLog(ex.Message);
                    return null;
                }
            }
        }    
}
