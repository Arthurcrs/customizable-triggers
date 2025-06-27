package com.mahghuuuls.configurabletriggers.core.condition.impl;

import com.google.gson.JsonObject;
import com.mahghuuuls.configurabletriggers.annotation.RegisterCondition;
import com.mahghuuuls.configurabletriggers.core.condition.ICondition;
import com.mahghuuuls.configurabletriggers.core.context.Context;
import com.mahghuuuls.configurabletriggers.core.context.CtxKey;
import com.mahghuuuls.configurabletriggers.core.context.CtxKeys;

import net.minecraft.entity.EntityLivingBase;

/**
 * Condition that compares an entity attribute with a threshold using a
 * configurable comparison operator.
 */
@RegisterCondition("entity_attribute_compare")
public final class EntityAttributeCompareCondition implements ICondition {

    private enum Attribute {
        HEALTH("health"),
        MAX_HEALTH("max_health"),
        HEALTH_PERCENTAGE("health_percentage"),
        ARMOR("armor");

        private final String id;

        Attribute(String id) {
            this.id = id;
        }

        static Attribute fromString(String str) {
            for (Attribute a : values()) {
                if (a.id.equalsIgnoreCase(str)) {
                    return a;
                }
            }
            throw new IllegalArgumentException("Unknown attribute: " + str);
        }
    }

    private enum Comparison {
        LESS("less") {
            @Override
            boolean test(double v, double t) { return v < t; }
        },
        HIGHER("higher") {
            @Override
            boolean test(double v, double t) { return v > t; }
        },
        EQUAL("equal") {
            @Override
            boolean test(double v, double t) { return v == t; }
        },
        LESS_OR_EQUAL("less_or_equal") {
            @Override
            boolean test(double v, double t) { return v <= t; }
        },
        HIGHER_OR_EQUAL("higher_or_equal") {
            @Override
            boolean test(double v, double t) { return v >= t; }
        };

        private final String id;

        Comparison(String id) {
            this.id = id;
        }

        abstract boolean test(double value, double threshold);

        static Comparison fromString(String str) {
            for (Comparison c : values()) {
                if (c.id.equalsIgnoreCase(str)) {
                    return c;
                }
            }
            throw new IllegalArgumentException("Unknown comparison: " + str);
        }
    }

    private final Attribute attribute;
    private final Comparison comparison;
    private final double threshold;
    private final CtxKey<?> entityKey;

    private static final String FIELD_ENTITY = "entity";
    private static final String FIELD_ATTRIBUTE = "attribute";
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_COMPARISON = "comparison";

    private EntityAttributeCompareCondition(CtxKey<?> entityKey, Attribute attribute,
            Comparison comparison, double threshold) {
        this.entityKey = entityKey;
        this.attribute = attribute;
        this.comparison = comparison;
        this.threshold = threshold;
    }

    @Override
    public boolean test(Context ctx) {
        EntityLivingBase entity = ctx.get(entityKey, EntityLivingBase.class);
        if (entity == null) {
            return false;
        }

        double value;
        switch (attribute) {
        case HEALTH:
            value = entity.getHealth();
            break;
        case MAX_HEALTH:
            value = entity.getMaxHealth();
            break;
        case HEALTH_PERCENTAGE:
            if (entity.getMaxHealth() <= 0)
                return false;
            value = (entity.getHealth() / entity.getMaxHealth()) * 100.0;
            break;
        case ARMOR:
            value = entity.getTotalArmorValue();
            break;
        default:
            return false;
        }

        return comparison.test(value, threshold);
    }

    /**
     * Creates an instance of this condition from its JSON representation.
     * Required fields are "entity", "attribute", "comparison" and "value".
     */
    public static EntityAttributeCompareCondition fromJson(JsonObject jsonObj) {
        if (!jsonObj.has(FIELD_ENTITY)) {
            throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ENTITY);
        }
        if (!jsonObj.has(FIELD_ATTRIBUTE)) {
            throw new IllegalArgumentException("Condition is missing required field: " + FIELD_ATTRIBUTE);
        }
        if (!jsonObj.has(FIELD_COMPARISON)) {
            throw new IllegalArgumentException("Condition is missing required field: " + FIELD_COMPARISON);
        }
        if (!jsonObj.has(FIELD_VALUE)) {
            throw new IllegalArgumentException("Condition is missing required field: " + FIELD_VALUE);
        }

        String entityName = jsonObj.get(FIELD_ENTITY).getAsString();
        String attrName = jsonObj.get(FIELD_ATTRIBUTE).getAsString();
        String comparisonName = jsonObj.get(FIELD_COMPARISON).getAsString();
        double value = jsonObj.get(FIELD_VALUE).getAsDouble();

        CtxKey<?> key = CtxKeys.getKeyFromId(entityName);
        Attribute attribute = Attribute.fromString(attrName);
        Comparison comp = Comparison.fromString(comparisonName);

        return new EntityAttributeCompareCondition(key, attribute, comp, value);
    }
}