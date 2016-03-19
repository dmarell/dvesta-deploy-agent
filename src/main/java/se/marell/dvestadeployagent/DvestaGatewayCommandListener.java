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
                    Runtime.getRuntime().exec(cmd);
                    logger.info("deploy succeeded");
                } catch (IOException e) {
                    logger.error("Failed to download artifact: {}", e.getMessage());
                    sender.sendResponse(messageId, "deploy failed: " + e.getMessage());
                }
            }
        } else {
            String msg = "Unknown command: " + commandName;
            logger.warn(msg);
            sender.sendResponse(messageId, msg);
        }
    }
}
