package org.emonocot.portal.remoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.acls.model.Permission;
import org.springframework.web.client.RestTemplate;
import org.emonocot.portal.model.AceDto;
import org.emonocot.model.common.SecuredObject;
import org.emonocot.model.user.Principal;
import org.emonocot.model.user.Group;
import org.emonocot.service.impl.UserServiceImpl;

public class RemoteUserServiceImpl extends UserServiceImpl {

    /**
    *
    */
    private String baseUri;

    /**
    *
    */
    private RestTemplate restTemplate;

    /**
    *
    */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 
     * @param newBaseUri
     *            Set the base uri
     */
    public final void setBaseUri(final String newBaseUri) {
        this.baseUri = newBaseUri;
    }

    /**
     * 
     * @param object
     *            Set the object being secured
     * @param recipient
     *            Set the principal who has the permission
     * @param permission
     *            Set the permission
     * @param clazz
     *            Set the class of the object
     */
    @Override
    public void addPermission(SecuredObject object,
            Principal recipient, Permission permission, Class<? extends SecuredObject> clazz) {
        AceDto aceDto = new AceDto();
        aceDto.setPrincipal(recipient);
        aceDto.setPermission(permission);
        aceDto.setObject(object);
        String uri = null;
        if(recipient instanceof Group) {
            uri = baseUri + "group/" + recipient.getIdentifier() + "/permission";
        } else {
            uri = baseUri + "user/" + recipient.getIdentifier() + "/permission"; 
        }
        restTemplate.postForObject(uri, aceDto, AceDto.class);
    }

    /**
     * 
     * @param object
     *            Set the object being secured
     * @param recipient
     *            Set the principal who has the permission
     * @param permission
     *            Set the permission
     * @param clazz
     *            Set the class of the object
     */
    @Override
    public void deletePermission(SecuredObject object,
            Principal recipient, Permission permission, Class<? extends SecuredObject> clazz) {
        AceDto aceDto = new AceDto();
        aceDto.setPrincipal(recipient);
        aceDto.setPermission(permission);
        aceDto.setObject(object);
        HttpEntity<AceDto> httpEntity = new HttpEntity<AceDto>(aceDto);
        String uri = null;
        if(recipient instanceof Group) {
            uri = baseUri + "group/" + recipient.getIdentifier() + "/permission?delete=true";
        } else {
            uri = baseUri + "user/" + recipient.getIdentifier() + "/permission?delete=true"; 
        }
        restTemplate.postForObject(uri, aceDto, AceDto.class);
    }
}
