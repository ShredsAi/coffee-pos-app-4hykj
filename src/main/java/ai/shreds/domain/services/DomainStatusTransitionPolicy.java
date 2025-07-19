package ai.shreds.domain.services;

import ai.shreds.shared.utils.SharedProductStatusEnum;
import ai.shreds.domain.exceptions.DomainInvalidStatusTransitionException;
import org.springframework.stereotype.Service;
import java.util.EnumSet;

/**
 * Policy to validate allowed status transitions for products and SKUs.
 */
@Service
public class DomainStatusTransitionPolicy {
    private static final EnumSet<SharedProductStatusEnum> ACTIVE_TRANSITIONS =
        EnumSet.of(SharedProductStatusEnum.INACTIVE, SharedProductStatusEnum.DISCONTINUED);
    private static final EnumSet<SharedProductStatusEnum> INACTIVE_TRANSITIONS =
        EnumSet.of(SharedProductStatusEnum.ACTIVE, SharedProductStatusEnum.DISCONTINUED);
    private static final EnumSet<SharedProductStatusEnum> DISCONTINUED_TRANSITIONS =
        EnumSet.noneOf(SharedProductStatusEnum.class);

    public void validateProductStatusChange(SharedProductStatusEnum current, SharedProductStatusEnum target) {
        if (!isValidTransition(current, target)) {
            throw new DomainInvalidStatusTransitionException(current, target);
        }
    }

    public void validateSkuStatus(SharedProductStatusEnum productStatus, SharedProductStatusEnum skuStatus) {
        if (SharedProductStatusEnum.ACTIVE.equals(skuStatus)
                && !SharedProductStatusEnum.ACTIVE.equals(productStatus)) {
            throw new DomainInvalidStatusTransitionException(productStatus, skuStatus);
        }
    }

    private boolean isValidTransition(SharedProductStatusEnum from, SharedProductStatusEnum to) {
        switch (from) {
            case ACTIVE:
                return ACTIVE_TRANSITIONS.contains(to);
            case INACTIVE:
                return INACTIVE_TRANSITIONS.contains(to);
            case DISCONTINUED:
                return DISCONTINUED_TRANSITIONS.contains(to);
            default:
                return false;
        }
    }
}
