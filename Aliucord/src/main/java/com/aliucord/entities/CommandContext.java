/*
 * Copyright (c) 2021 Juby210 & Vendicated
 * Licensed under the Open Software License version 3.0
 */

package com.aliucord.entities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aliucord.wrappers.ChannelWrapper;
import com.discord.api.message.LocalAttachment;
import com.discord.api.message.MessageReference;
import com.discord.models.member.GuildMember;
import com.discord.models.message.Message;
import com.discord.models.user.MeUser;
import com.discord.models.user.User;
import com.discord.stores.StoreStream;
import com.discord.utilities.SnowflakeUtils;
import com.discord.utilities.attachments.AttachmentUtilsKt;
import com.discord.widgets.chat.MessageContent;
import com.discord.widgets.chat.input.*;
import com.lytefast.flexinput.model.Attachment;

import java.util.*;

/** Context passed to command executors */
@SuppressWarnings({ "unused"})
public class CommandContext {
    private final Map<String, ?> args;
    private final WidgetChatInput$configureSendListeners$2 _this;
    private final MessageContent messageContent;
    private final ChatInputViewModel.ViewState.Loaded viewState;
    private List<Attachment<?>> attachments;

    @SuppressWarnings("unchecked")
    public CommandContext(Map<String, ?> args, WidgetChatInput$configureSendListeners$2 _this, Object[] _args, MessageContent messageContent) {
        this.args = args;
        this._this = _this;
        this.messageContent = messageContent;
        this.attachments = (List<Attachment<?>>) _args[0];
        viewState = ((WidgetChatInput$configureSendListeners$6$1) _args[2]).this$0.$viewState;
    }

    private static <T> T requireNonNull(String key, T val) {
        return Objects.requireNonNull(val, String.format("Required argument %s was null", key));
    }

    /** Returns the ViewState associated with this Context */
    @NonNull
    public ChatInputViewModel.ViewState.Loaded getViewState() {
        return viewState;
    }

    /** Returns the AppContext */
    public Context getContext() {
        return _this.$context;
    }

    /** Returns the maximum size attachments may be */
    public int getMaxFileSizeMB() {
        return viewState.getMaxFileSizeMB();
    }

    @Nullable
    public ChatInputViewModel.ViewState.Loaded.PendingReplyState.Replying getReplyingState() {
        ChatInputViewModel.ViewState.Loaded.PendingReplyState state = viewState.getPendingReplyState();
        if (state instanceof  ChatInputViewModel.ViewState.Loaded.PendingReplyState.Replying) return (ChatInputViewModel.ViewState.Loaded.PendingReplyState.Replying) state;
        return null;
    }

    /** Returns the MessageReference */
    @Nullable
    public MessageReference getMessageReference() {
        ChatInputViewModel.ViewState.Loaded.PendingReplyState.Replying state = getReplyingState();
        return state != null ? state.getMessageReference() : null;
    }

    /** Returns the Author of the referenced message */
    @Nullable
    public User getReferencedMessageAuthor() {
        ChatInputViewModel.ViewState.Loaded.PendingReplyState.Replying state = getReplyingState();
        return state != null ? state.getRepliedAuthor() : null;
    }

    /** Returns the Author of the referenced message as member of the current guild */
    @Nullable
    public GuildMember getReferencedMessageAuthorGuildMember() {
        ChatInputViewModel.ViewState.Loaded.PendingReplyState.Replying state = getReplyingState();
        return state != null ? state.getRepliedAuthorGuildMember() : null;
    }

    /** Returns the referenced message */
    @Nullable
    public Message getReferencedMessage() {
        MessageReference ref = getMessageReference();
        if (ref == null) return null;
        return StoreStream.getMessages().getMessage(ref.a(), ref.c());
    }

    /** Returns the link of the referenced message */
    @Nullable
    public String getReferencedMessageLink() {
        MessageReference ref = getMessageReference();
        if (ref == null) return null;
        String guildId = ref.b() != null ? String.valueOf(ref.b()) : "@me";
        return String.format(Locale.ENGLISH, "https://discord.com/channels/%s/%d/%d", guildId, ref.a(), ref.c());
    }

    /** Returns the current channel id */
    public long getChannelId() {
        return _this.$chatInput.getChannelId();
    }

    /** Sets the current channel id */
    public void setChannelId(long id) {
        _this.$chatInput.setChannelId(id);
    }

    /** Returns the current channel */
    @NonNull
    public ChannelWrapper getChannel() {
        return new ChannelWrapper(viewState.getChannel());
    }

    /** Returns the raw content of the message that invoked this command */
    public String getRawContent() {
        return messageContent.getTextContent();
    }

    /** Returns the attachments of the message that invoked this command */
    @NonNull
    public List<Attachment<?>> getAttachments() {
        return attachments;
    }

    /**
     * Adds an attachment
     * @param uri Uri of the attachment
     * @param displayName file name
     */
    public void addAttachment(String uri, String displayName) {
        addAttachment(new LocalAttachment(SnowflakeUtils.fromTimestamp(System.currentTimeMillis()), uri, displayName));
    }

    /**
     * Adds an attachment
     * @param attachment Attachment
     */
    public void addAttachment(LocalAttachment attachment) {
        addAttachment(AttachmentUtilsKt.toAttachment(attachment));
    }

    /**
     * Adds an attachment
     * @param attachment Attachment
     */
    public void addAttachment(Attachment<?> attachment) {
        if (!(attachments instanceof ArrayList)) attachments = new ArrayList<>(attachments);
        attachments.add(attachment);
    }

    /** Returns the mentioned users */
    public List<User> getMentionedUsers() {
        return messageContent.getMentionedUsers();
    }

    /** Returns the current user */
    @NonNull
    public MeUser getMe() {
        return StoreStream.getUsers().getMe();
    }

    /** Returns the raw args */
    @NonNull
    public Map<String, ?> getRawArgs() {
        return args;
    }

    /**
     * Check if the arguments contain the specified key
     * @param key Key to check
     */
    public boolean containsArg(String key) {
        return args.containsKey(key);
    }

    /**
     * Gets the arguments object for the specified subcommand
     * @param key Key of the subcommand
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public Map<String, ?> getSubCommandArgs(String key) {
        return (Map<String, ?>) args.get(key);
    }

    /**
     * Gets the raw argument with the specified key
     * @param key The key of the argument
     */
    @Nullable
    public Object get(String key) {
        return args.get(key);
    }

    /**
     * Gets the <strong>required</strong> raw argument with the specified key
     * @param key The key of the argument
     */
    @NonNull
    public Object getRequired(String key) {
        return requireNonNull(key, get(key));
    }

    /**
     * Gets the raw argument with the specified key or the defaultValue if no such argument is present
     * @param key The key of the argument
     * @param defaultValue The default value
     */
    @NonNull
    public Object getOrDefault(String key, Object defaultValue) {
        Object val = get(key);
        return val != null ? val : defaultValue;
    }

    /**
     * Gets the String argument with the specified key
     * @param key The key of the argument
     */
    @Nullable
    public String getString(String key) {
        return (String) args.get(key);
    }

    /**
     * Gets the <strong>required</strong> String argument with the specified key
     * @param key The key of the argument
     */
    @NonNull
    public String getRequiredString(String key) {
        return requireNonNull(key, getString(key));
    }

    /**
     * Gets the String argument with the specified key or the defaultValue if no such argument is present
     * @param key The key of the argument
     */
    @NonNull
    public String getStringOrDefault(String key, @NonNull String defaultValue) {
        String val = getString(key);
        return val != null ? val : defaultValue;
    }

    /**
     * Gets the Integer argument with the specified key
     * @param key The key of the argument
     */
    @Nullable
    public Integer getInt(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof String) return Integer.valueOf((String) val);
        throw new ClassCastException(String.format("Argument %s is of type %s which cannot be cast to Integer.", key, val.getClass().getSimpleName()));
    }

    /**
     * Gets the <strong>required</strong> Integer argument with the specified key
     * @param key The key of the argument
     */
    public int getRequiredInt(String key) {
        return requireNonNull(key, getInt(key));
    }

    /**
     * Gets the Integer argument with the specified key or the defaultValue if no such argument is present
     * @param key The key of the argument
     */
    public int getIntOrDefault(String key, int defaultValue) {
        Integer val = getInt(key);
        return val != null ? val : defaultValue;
    }

    /**
     * Gets the Long argument with the specified key
     * @param key The key of the argument
     */
    @Nullable
    public Long getLong(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof Long) return (Long) val;
        if (val instanceof String) return Long.valueOf((String) val);
        throw new ClassCastException(String.format("Argument %s is of type %s which cannot be cast to Long.", key, val.getClass().getSimpleName()));
    }

    /**
     * Gets the <strong>required</strong> Long argument with the specified key
     * @param key The key of the argument
     */
    public long getRequiredLong(String key) {
        return requireNonNull(key, getLong(key));
    }

    /**
     * Gets the Long argument with the specified key or the defaultValue if no such argument is present
     * @param key The key of the argument
     */
    public long getLongOrDefault(String key, long defaultValue) {
        Long val = getLong(key);
        return val != null ? val : defaultValue;
    }

    /**
     * Gets the Boolean argument with the specified key
     * @param key The key of the argument
     */
    @Nullable
    public Boolean getBool(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof Boolean) return (Boolean) val;
        if (val instanceof String) return Boolean.valueOf((String) val);
        throw new ClassCastException(String.format("Argument %s is of type %s which cannot be cast to Boolean.", key, val.getClass().getSimpleName()));

    }

    /**
     * Gets the <strong>required</strong> Boolean argument with the specified key
     * @param key The key of the argument
     */
    public boolean getRequiredBool(String key) {
        return requireNonNull(key, getBool(key));
    }

    /**
     * Gets the Boolean argument with the specified key or the defaultValue if no such argument is present
     * @param key The key of the argument
     */
    public boolean getBoolOrDefault(String key, boolean defaultValue) {
        Boolean val = getBool(key);
        return val != null ? val : defaultValue;
    }

    /**
     * Gets the User argument with the specified key
     * @param key The key of the argument
     */
    @Nullable
    public User getUser(String key) {
        Long id = getLong(key);
        return id != null ? StoreStream.getUsers().getUsers().get(id) : null;
    }

    /**
     * Gets the <strong>required</strong> User argument with the specified key
     * @param key The key of the argument
     */
    @NonNull
    public User getRequiredUser(String key) {
        return requireNonNull(key, getUser(key));
    }

    /**
     * Gets the User argument with the specified key or the defaultValue if no such argument is present
     * @param key The key of the argument
     */
    @NonNull
    public User getUserOrDefault(String key, User defaultValue) {
        User val = getUser(key);
        return val != null ? val : defaultValue;
    }
}
