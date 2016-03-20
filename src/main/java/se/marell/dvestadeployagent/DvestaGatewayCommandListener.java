/*
 * Created by Daniel Marell 17/03/16.
 */
package se.marell.dvestadeployagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.marell.dvestagatewayclient.CommandResponseSender;
import se.marell.dvestagatewayclient.GatewayCommandListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
class DvestaGatewayCommandListener implements GatewayCommandListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public void command(CommandResponseSender sender, String messageId, String commandName, List<String> commandArgs) {
        logger.info("command: {}, num args: {}", commandName, commandArgs.size());
        if (commandName.equals("deploy")) {
            if (commandArgs.size() != 5) {
                String msg = "deploy: Expected 5 arguments, got " + commandArgs.size();
                logger.warn(msg);
                sender.sendResponse(commandName, msg);
            } else {
                String artifactDownloadUser = commandArgs.get(0);
                String artifactDownloadPassword = commandArgs.get(1);
                String serviceName = commandArgs.get(2);
                String installDir = commandArgs.get(3);
                String artifactDownloadUrl = commandArgs.get(4);
                logger.info(" deploy, artifactDownloadUser: {}, artifactDownloadPassword: {}, " +
                                "serviceName: {}, installDir: {}, artifactDownloadUrl: {}",
                        artifactDownloadUser, artifactDownloadPassword, serviceName, installDir, artifactDownloadUrl);
                try {
                    // serviceName installDir artifactDownloadUrl
                    logger.info("deploying {}", serviceName);
                    String cmd = String.format("sh deploy.sh -u %s -p %s %s %s %s",
                            artifactDownloadUser,
                            artifactDownloadPassword,
                            serviceName,
                            installDir,
                            artifactDownloadUrl
                    );
                    CommandOutput out = executeCommand(cmd);
                    logger.debug("deploy command output: {}", out);
                    if (out.exitCode == 0) {
                        logger.info("deploy script succeeded");
                    } else {
                        logger.info("deploy script failed with exit code: {}, error: {}", out.exitCode, out.errorOutput);
                    }
                } catch (IOException e) {
                    String msg = "Execution of deploy script failed: " + e.getMessage();
                    logger.error(msg);
                    sender.sendResponse(messageId, msg);
                }
            }
        } else {
            String msg = "Unknown command: " + commandName;
            logger.warn(msg);
            sender.sendResponse(messageId, msg);
        }
    }

    private CommandOutput executeCommand(String cmd) throws IOException {
        Process p = Runtime.getRuntime().exec(cmd);
        int exitCode;
        try {
            exitCode = p.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted");
        }
        return new CommandOutput(
                exitCode,
                convertStreamToString(p.getInputStream()),
                convertStreamToString(p.getErrorStream()));
    }

    private static class CommandOutput {
        int exitCode;
        String standardOutput;
        String errorOutput;

        public CommandOutput(int exitCode, String standardOutput, String errorOutput) {
            this.exitCode = exitCode;
            this.standardOutput = standardOutput;
            this.errorOutput = errorOutput;
        }

        @Override
        public String toString() {
            return "CommandOutput{" +
                    "exitCode=" + exitCode +
                    ", standardOutput='" + standardOutput + '\'' +
                    ", errorOutput='" + errorOutput + '\'' +
                    '}';
        }
    }
}
