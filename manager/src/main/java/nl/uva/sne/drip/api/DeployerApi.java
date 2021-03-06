/**
 * NOTE: This class is auto generated by the swagger code generator program (2.4.10).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package nl.uva.sne.drip.api;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-12-06T13:31:49.386Z")

@Api(value = "deployer", description = "the deployer API")
public interface DeployerApi {

    @ApiOperation(value = "deploy the software tosca template", nickname = "deployProvisionToscaTemplateByID", notes = "Returns the deployment ID", response = String.class, authorizations = {
        @Authorization(value = "auth", scopes = {
            @AuthorizationScope(scope = "read:ToscaTemplate", description = "read your topolog template")
            ,
            @AuthorizationScope(scope = "write:ToscaTemplate", description = "modify topolog template in your account")
        })
    }, tags = {})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation", response = String.class)
        ,
        @ApiResponse(code = 400, message = "Invalid ID supplied")
        ,
        @ApiResponse(code = 404, message = "ToscaTemplate not found")
        ,
        @ApiResponse(code = 405, message = "Invalid input")})
    @RequestMapping(value = "/manager/deployer/deploy/{id}",
            produces = {"text/plain"},
            method = RequestMethod.GET)
    ResponseEntity<String> deployProvisionToscaTemplateByID(@ApiParam(value = "ID of topolog template to deploy", required = true) @PathVariable("id") String id);

}
