/*
 * Created by Daniel Marell 17/03/16.
 */
package se.marell.dvestadeployagent;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import se.marell.dvesta.ioscan.IoScanSpringConfig;
import se.marell.dvesta.system.SystemSpringConfig;
import se.marell.dvesta.tickengine.TickEngineSpringConfig;
import se.marell.dvestagatewayclient.DvestaGatewayClientSpringConfig;

@Configuration
@Import(DvestaGatewayClientSpringConfig.class)
public class Config {
}
