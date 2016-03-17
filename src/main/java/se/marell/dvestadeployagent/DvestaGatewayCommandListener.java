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
import java.util.List;

@Component
class DvestaGatewayCommandListener implements GatewayCommandListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void command(CommandResponseSender sender, String messageId, String commandName, List<String> commandArgs) {
        if (commandName.equals("deploy")) {
            if (commandArgs.size() != 7) {
                String msg = "update: Expected 7 arguments, got " + commandArgs.size();
                logger.error(msg);
                sender.sendResponse(commandName, msg);
            } else {
                String artifactDownloadUrl = commandArgs.get(0);
                String artifactDownloadUser = commandArgs.get(1);
                String artifactDownloadPassword = commandArgs.get(2);
                String artifactFilename = commandArgs.get(3);
                String installDir = commandArgs.get(4);
                String jarFile = commandArgs.get(5);
                String serviceName = commandArgs.get(6);
                try {
                    String cmd = String.format("sh deploy.sh %s %s %s %s %s %s %s",
                            artifactDownloadUrl,
                            artifactDownloadUser,
                            artifactDownloadPassword,
                            artifactFilename,
                            installDir,
                            jarFile,
                            serviceName);
                    Runtime.getRuntime().exec(cmd);
                    logger.info("deploy succeeded");
                } catch (IOException e) {
                    logger.error("Failed to download artifact: {}", e.getMessage());
                    sender.sendResponse(messageId, "deploy failed: " + e.getMessage());
                }
            }
        } else {
            sender.sendResponse(messageId, "Unknown command: " + commandName);
        }
    }
}
