package com.sfl.nms.services.notification.push;

import com.sfl.nms.services.device.model.UserDevice;
import com.sfl.nms.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscription;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscriptionRequest;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscriptionRequestState;
import com.sfl.nms.services.test.AbstractServiceIntegrationTest;
import com.sfl.nms.services.user.model.User;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 9:22 AM
 */
public class PushNotificationSubscriptionRequestServiceIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    /* Constructors */
    public PushNotificationSubscriptionRequestServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testUpdatePushNotificationSubscriptionRequestRecipient() {
        // Prepare data
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        final PushNotificationRecipient recipient = getServicesTestHelper().createPushNotificationSnsRecipient(subscription, getServicesTestHelper().createPushNotificationSnsRecipientDto());
        final UserDevice userMobileDevice = getServicesTestHelper().createUserDevice(subscription.getUser(), getServicesTestHelper().createUserDeviceDto());
        PushNotificationSubscriptionRequest request = getServicesTestHelper().createPushNotificationSubscriptionRequest(subscription.getUser(), userMobileDevice, getServicesTestHelper().createPushNotificationSubscriptionRequestDto());
        assertNull(request.getRecipient());
        flushAndClear();
        // Update recipient for request
        request = pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestRecipient(request.getId(), recipient.getId());
        assertPushNotificationSubscriptionRequestRecipient(request, recipient);
        // Flush, clear, reload and assert again
        flushAndClear();
        request = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(request.getId());
        assertPushNotificationSubscriptionRequestRecipient(request, recipient);
    }

    private void assertPushNotificationSubscriptionRequestRecipient(final PushNotificationSubscriptionRequest request, final PushNotificationRecipient recipient) {
        assertNotNull(request);
        assertNotNull(request.getRecipient());
        assertEquals(recipient.getId(), request.getRecipient().getId());
    }

    @Test
    public void testCheckIfPushNotificationSubscriptionRecipientExistsForUuId() {
        // Prepare data
        final PushNotificationSubscriptionRequest request = getServicesTestHelper().createPushNotificationSubscriptionRequest();
        // Check if item exists
        boolean result = pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(request.getUuId() + "_u");
        assertFalse(result);
        result = pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(request.getUuId());
        assertTrue(result);
        // Flush, clear, reload and assert again
        flushAndClear();
        result = pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(request.getUuId());
        assertTrue(result);
    }

    @Test
    public void testGetPushNotificationSubscriptionRequestByUuId() {
        // Prepare data
        final PushNotificationSubscriptionRequest request = getServicesTestHelper().createPushNotificationSubscriptionRequest();
        // Load by uuid
        PushNotificationSubscriptionRequest result = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestByUuId(request.getUuId());
        assertEquals(request, result);
        // Flush, clear, reload and assert
        flushAndClear();
        result = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestByUuId(request.getUuId());
        assertEquals(request, result);
    }

    @Test
    public void testCreatePushNotificationSubscriptionRequest() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final UserDevice userMobileDevice = getServicesTestHelper().createUserDevice(user, getServicesTestHelper().createUserDeviceDto());
        final PushNotificationSubscriptionRequestDto requestDto = getServicesTestHelper().createPushNotificationSubscriptionRequestDto();
        flushAndClear();
        // Create request
        PushNotificationSubscriptionRequest request = pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(user.getId(), userMobileDevice.getId(), requestDto);
        assertPushNotificationSubscriptionRequest(request, requestDto, user, userMobileDevice);
        // Flush, clear, reload and assert
        flushAndClear();
        request = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(request.getId());
        assertPushNotificationSubscriptionRequest(request, requestDto, user, userMobileDevice);
    }

    @Test
    public void testGetPushNotificationSubscriptionRequestById() {
        // Prepare data
        final PushNotificationSubscriptionRequest request = getServicesTestHelper().createPushNotificationSubscriptionRequest();
        // Load request by id and assert
        PushNotificationSubscriptionRequest result = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(request.getId());
        assertEquals(request, result);
        // Flush, clear, reload and assert
        flushAndClear();
        result = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(request.getId());
        assertEquals(request, result);
    }

    @Test
    public void testUpdatePushNotificationSubscriptionRequestState() {
        // Prepare data
        PushNotificationSubscriptionRequest request = getServicesTestHelper().createPushNotificationSubscriptionRequest();
        assertEquals(PushNotificationSubscriptionRequestState.CREATED, request.getState());
        final PushNotificationSubscriptionRequestState updatedState = PushNotificationSubscriptionRequestState.PROCESSING;
        flushAndClear();
        // Update state
        request = pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(request.getId(), updatedState);
        assertEquals(updatedState, request.getState());
        // Flush, clear, reload and assert
        flushAndClear();
        request = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(request.getId());
        assertEquals(updatedState, request.getState());
    }


    /* Utility methods */
    private void assertPushNotificationSubscriptionRequest(final PushNotificationSubscriptionRequest request, final PushNotificationSubscriptionRequestDto requestDto, final User user, final UserDevice userMobileDevice) {
        getServicesTestHelper().assertPushNotificationSubscriptionRequest(request, requestDto);
        assertNotNull(request.getUser());
        assertEquals(user.getId(), request.getUser().getId());
        assertNotNull(request.getUserMobileDevice());
        assertEquals(userMobileDevice.getId(), request.getUserMobileDevice().getId());
    }
}
