/*
 * Copyright (c) 2021 Juby210 & Vendicated
 * Licensed under the Open Software License version 3.0
 */

package com.aliucord.api;

import androidx.annotation.NonNull;

import com.aliucord.patcher.*;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import top.canyie.pine.callback.MethodHook;

@SuppressWarnings({"unused", "deprecation"})
public class PatcherAPI {
    /**
     * @deprecated Use {@link PatcherAPI#patch(String, String, Class[], MethodHook)}, {@link PatcherAPI#patch(Class, String, Class[], MethodHook)} or {@link PatcherAPI#patch(Member, MethodHook)} instead.
     */
    @Deprecated
    public static Runnable addPatch(String forClass, String fn, PatchFunction patch) {
        return Patcher.addPatch(forClass, fn, patch);
    }

    /**
     * @deprecated Use {@link PatcherAPI#patch(String, String, Class[], MethodHook)}, {@link PatcherAPI#patch(Class, String, Class[], MethodHook)} or {@link PatcherAPI#patch(Member, MethodHook)} instead.
     */
    @Deprecated
    public static Runnable addPrePatch(String forClass, String fn, PrePatchFunction patch) {
        return Patcher.addPrePatch(forClass, fn, patch);
    }

    public List<Runnable> unpatches = new ArrayList<>();

    private Runnable createUnpatch(Runnable _unpatch) {
        Runnable unpatch = new Runnable() {
            public void run() {
                _unpatch.run();
                unpatches.remove(this);
            }
        };
        unpatches.add(unpatch);
        return unpatch;
    }

    /**
     * Patches a method.
     * @param forClass Class to patch.
     * @param fn Method to patch.
     * @param paramTypes Parameters of the <code>fn</code>. Useful for patching individual overloads.
     * @param hook Callback for the patch.
     * @return A {@link Runnable} object.
     * @see PinePatchFn
     * @see PinePrePatchFn
     */
    public Runnable patch(@NonNull String forClass, @NonNull String fn, @NonNull Class<?>[] paramTypes, @NonNull MethodHook hook) {
        return createUnpatch(Patcher.addPatch(forClass, fn, paramTypes, hook));
    }

    /**
     * Patches a method.
     * @param clazz Class to patch.
     * @param fn Method to patch.
     * @param paramTypes Parameters of the <code>fn</code>. Useful for patching individual overloads.
     * @param hook Callback for the patch.
     * @return Method that will remove the patch when invoked
     * @see PinePatchFn
     * @see PinePrePatchFn
     */
    public Runnable patch(@NonNull Class<?> clazz, @NonNull String fn, @NonNull Class<?>[] paramTypes, @NonNull MethodHook hook) {
        return createUnpatch(Patcher.addPatch(clazz, fn, paramTypes, hook));
    }

    /**
     * Patches a method or constructor.
     * @param m Method or constructor to patch. see {@link Member}.
     * @param hook Callback for the patch.
     * @return Method that will remove the patch when invoked
     * @see PatcherAPI#patch(String, String, Class[], MethodHook)
     * @see PatcherAPI#patch(Class, String, Class[], MethodHook)
     * @see PinePatchFn
     * @see PinePrePatchFn
     */
    public Runnable patch(@NonNull Member m, @NonNull MethodHook hook) {
        return createUnpatch(Patcher.addPatch(m, hook));
    }

    /**
     * @deprecated Use {@link PatcherAPI#patch(String, String, Class[], MethodHook)}, {@link PatcherAPI#patch(Class, String, Class[], MethodHook)} or {@link PatcherAPI#patch(Member, MethodHook)} instead.
     */
    @Deprecated
    public Runnable patch(String forClass, String fn, PatchFunction patch) {
        Runnable unpatch = PatcherAPI.addPatch(forClass, fn, patch);
        Runnable _unpatch = new Runnable() {
            public void run() {
                unpatch.run();
                unpatches.remove(this);
            }
        };
        unpatches.add(_unpatch);
        return _unpatch;
    }

    /**
     * @deprecated Use {@link PatcherAPI#patch(String, String, Class[], MethodHook)}, {@link PatcherAPI#patch(Class, String, Class[], MethodHook)} or {@link PatcherAPI#patch(Member, MethodHook)} instead.
     */
    @Deprecated
    public Runnable prePatch(String forClass, String fn, PrePatchFunction patch) {
        Runnable unpatch = PatcherAPI.addPrePatch(forClass, fn, patch);
        Runnable _unpatch = new Runnable() {
            public void run() {
                unpatch.run();
                unpatches.remove(this);
            }
        };
        unpatches.add(_unpatch);
        return _unpatch;
    }

    /**
     * Removes all patches.
     */
    public void unpatchAll() {
        Object[] runnables = unpatches.toArray();
        for (Object unpatch : runnables) ((Runnable) unpatch).run();
    }
}
