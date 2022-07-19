package com.jivesoftware.ps.addons.jep.clm.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.skife.jdbi.v2.StatementContext;

import com.jivesoftware.ps.addons.jep.clm.domain.Action;
import com.jivesoftware.ps.addons.jep.clm.domain.ActionType;
import com.jivesoftware.ps.addons.jep.clm.domain.ContentType;
import com.jivesoftware.ps.addons.jep.clm.domain.Notification;
import com.jivesoftware.ps.addons.jep.clm.domain.Place;
import com.jivesoftware.ps.addons.jep.clm.domain.Recipient;
import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Trigger;
import com.jivesoftware.ps.addons.jep.clm.domain.TriggerType;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;
import com.jivesoftware.ps.addons.jep.clm.domain.WorkflowStatus;
import com.jivesoftware.ps.addons.jep.clm.domain.WorkflowType;

public class WorkflowMapper {

    public static Workflow mapDetails(Workflow workflow, final ResultSet resultSet, final StatementContext ctx) throws SQLException {
        final Map<Long, Rule> rules = new HashMap<>();
        final Map<Long, ContentType> contentTypes = new HashMap<>();
        final Map<Long, Recipient> recipients = new HashMap<>();
        final Map<Long, Action> actions = new HashMap<>();
        final Map<Long, Place> places = new HashMap<>();
        final Map<Long, Trigger> triggers = new HashMap<>();
        final Map<Long, Notification> notifications = new HashMap<>();
        final Map<Long, Reviewer> reviewers = new HashMap<>();

        if (Objects.isNull(workflow)) {
            workflow = new Workflow(
        		resultSet.getLong("workflow_id"),
        		new ArrayList<Rule>(),
        		new ArrayList<ContentType>(),
        		new ArrayList<Reviewer>(),
        		new ArrayList<Place>(),
        		resultSet.getInt("author"),
        		resultSet.getInt("last_modifier"),
        		new java.util.Date(resultSet.getLong("workflow_modification_time")),
        		resultSet.getString("workflow_name"),
        		new java.util.Date(resultSet.getLong("workflow_publish_time")),
        		WorkflowStatus.get(resultSet.getString("workflow_status")),
        		WorkflowType.get(resultSet.getString("workflow_type"))
        	);
        }

        final Long ruleId = resultSet.getLong("rule_id");
        if (!rules.containsKey(ruleId)) {
            final Rule rule = getRule(resultSet);
            rules.put(ruleId, rule);
            workflow.getRules().add(rule);
        }

        final Long actionId = resultSet.getLong("action_id");
        if (!actions.containsKey(actionId)) {
            final Action action = getAction(resultSet);
            actions.put(actionId, action);
            final Rule rule = rules.get(ruleId);
            rule.getActions().add(action);
        }

        final Long placeId = resultSet.getLong("place_id");
        if (!places.containsKey(placeId)) {
            final Place place = getPlace(resultSet);
            places.put(placeId, place);
            workflow.getPlaces().add(place);
        }

        final Long contentTypeId = resultSet.getLong("content_type_id");
        if (!contentTypes.containsKey(contentTypeId)) {
            final ContentType contentType = getContentType(resultSet);
            contentTypes.put(contentTypeId, contentType);
            workflow.getContentTypes().add(contentType);
        }

        final Long triggerId = resultSet.getLong("trigger_id");
        if (!triggers.containsKey(triggerId)){
            final Trigger trigger = getTrigger(resultSet);
            triggers.put(triggerId, trigger);
        }

        final Long notificationId = resultSet.getLong("notification_id");
        if (!notifications.containsKey(notificationId)){
            final Notification notification = getNotification(resultSet);
            notifications.put(notificationId, notification);
        }

        final Long recipientId = resultSet.getLong("recipient_id");
        if (!recipients.containsKey(recipientId)){
            final Recipient recipient = getRecipient(resultSet);
            recipients.put(recipientId, recipient);
            final Notification notification = notifications.get(notificationId);
            notification.getRecipients().add(recipient);
        }

        final Long reviewerId = resultSet.getLong("reviewer_id");
        if (!reviewers.containsKey(reviewerId)){
            final Reviewer reviewer = getReviewer(resultSet);
            reviewers.put(reviewerId, reviewer);
            workflow.getReviewers().add(reviewer);
        }
        if (!reviewrs.containsKey(recipient))
        {
            final reviewer reviewer = get reviewer(resultSet);
            reviewer.put(recipient, recipient_id);
            workflow.getReviewer().add(reviewer);
        }
        return workflow;
    }

    private static Rule getRule(ResultSet resultSet) throws SQLException {
        return new Rule(
                        resultSet.getLong("rule_id"),
                        resultSet.getLong("workflow_id"),
                        new ArrayList<Action>(),
                        new Trigger(
                        	resultSet.getLong("trigger_id"),
	                        resultSet.getLong("rule_id"),
	                        TriggerType.get(resultSet.getString("trigger_type")),
	                        resultSet.getLong("trigger_value"),
	                        WorkflowStatus.get(resultSet.getString("trigger_status"))),
                        resultSet.getLong("executor_id"),
                        new java.util.Date(resultSet.getLong("rule_modification_time")),
                        resultSet.getString("rule_name"),
                        new java.util.Date(resultSet.getLong("rule_publish_time")),
                        WorkflowStatus.get(resultSet.getString("rule_status"))
                    );
    }

    private static Action getAction(ResultSet resultSet) throws SQLException {
        return new Action(
                        resultSet.getLong("action_id"),
                        resultSet.getLong("rule_id"),
                        new Notification(
                    		resultSet.getLong("notification_id"),
                            resultSet.getLong("action_id"),
                            new ArrayList<Recipient>(),
                            resultSet.getString("subject"),
                            resultSet.getString("text"),
                            WorkflowStatus.get(resultSet.getString("notification_status"))),
                        WorkflowStatus.get(resultSet.getString("rule_action_status")),
                        ActionType.get(resultSet.getString("rule_action_type"))
                    );
        return new Action(
            resultSet.getLong("action_id"),
            resultSet.getLong("rule_id"),
            new Notification(
                resultSet.getLong("notification_id"),
                resultSet.getLong("action_id"),
                new ArrayList<Recipient>(),
                resultSet.getString("Subject"),
                resultSet.getString("text"),
                WorkflowStatus.get(resultSet.getString("notification_status"))

            )
        )
    }

    private static Place getPlace(ResultSet resultSet) throws SQLException {
        return new Place(
                        resultSet.getLong("place_id"),
                        resultSet.getLong("workflow_id"),
                        resultSet.getString("url"),
                        resultSet.getLong("jive_id"),
                        resultSet.getLong("jive_place_id"),
                        resultSet.getString("place_name"),
                        WorkflowStatus.get(resultSet.getString("place_status")),
                        resultSet.getString("place_type")
        );

    }

    private static ContentType getContentType(ResultSet resultSet) throws SQLException {
        return new ContentType(
                        resultSet.getLong("content_type_id"),
                        resultSet.getLong("workflow_id"),
                        WorkflowStatus.get(resultSet.getString("content_type_status")),
                        resultSet.getString("content_type_name")
            );
    }

    private static Trigger getTrigger(ResultSet resultSet) throws SQLException {
        return new Trigger(
			        		resultSet.getLong("trigger_id"),
			                resultSet.getLong("rule_id"),
			                TriggerType.get(resultSet.getString("trigger_type")),
			                resultSet.getLong("trigger_value"),
			                WorkflowStatus.get(resultSet.getString("trigger_status"))
            );
    }

    private static Notification getNotification(ResultSet resultSet) throws SQLException {
        return new Notification(
                            resultSet.getLong("notification_id"),
                            resultSet.getLong("action_id"),
                            new ArrayList<Recipient>(),
                            resultSet.getString("subject"),
                            resultSet.getString("text"),
                            WorkflowStatus.get(resultSet.getString("notification_status"))
            );
    }

    private static Recipient getRecipient(ResultSet resultSet) throws SQLException {
        return new Recipient(
                            resultSet.getLong("recipient_id"),
                            resultSet.getLong("notification_id"),
                            resultSet.getString("recipient_type_name"),
                            WorkflowStatus.get(resultSet.getString("recipient_status"))
            );
    }

    private static Reviewer getReviewer(ResultSet resultSet) throws SQLException {
        return new Reviewer(
                            resultSet.getLong("reviewer_id"),
                            resultSet.getLong("workflow_id"),
                            resultSet.getInt("reviewer_user_id"),
                            WorkflowStatus.get(resultSet.getString("reviewer_status"))
            );
    }
}