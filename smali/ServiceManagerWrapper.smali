.class public Lcn/wzc/hookpms/ServiceManagerWrapper;
.super Ljava/lang/Object;
.source "ServiceManagerWrapper.java"


# direct methods
.method public constructor <init>()V
    .locals 0

    .line 19
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static hookPMS(Landroid/content/Context;)V
    .locals 3
    .param p0, "context"    # Landroid/content/Context;

    .line 51
    const-string v0, ""

    .line 52
    .local v0, "hookSign":Ljava/lang/String;
    const-string v1, ""

    const/4 v2, 0x0

    invoke-static {p0, v0, v1, v2}, Lcn/wzc/hookpms/ServiceManagerWrapper;->hookPMS(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;I)V

    .line 53
    return-void
.end method

.method public static hookPMS(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;I)V
    .locals 11
    .param p0, "context"    # Landroid/content/Context;
    .param p1, "signed"    # Ljava/lang/String;
    .param p2, "appPkgName"    # Ljava/lang/String;
    .param p3, "hashCode"    # I

    .line 24
    :try_start_0
    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 25
    .local v0, "activityThreadClass":Ljava/lang/Class;, "Ljava/lang/Class<*>;"
    const-string v1, "currentActivityThread"

    const/4 v2, 0x0

    new-array v3, v2, [Ljava/lang/Class;

    .line 26
    invoke-virtual {v0, v1, v3}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    .line 27
    .local v1, "currentActivityThreadMethod":Ljava/lang/reflect/Method;
    const/4 v3, 0x0

    new-array v4, v2, [Ljava/lang/Object;

    invoke-virtual {v1, v3, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    .line 29
    .local v3, "currentActivityThread":Ljava/lang/Object;
    const-string v4, "sPackageManager"

    invoke-virtual {v0, v4}, Ljava/lang/Class;->getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v4

    .line 30
    .local v4, "sPackageManagerField":Ljava/lang/reflect/Field;
    const/4 v5, 0x1

    invoke-virtual {v4, v5}, Ljava/lang/reflect/Field;->setAccessible(Z)V

    .line 31
    invoke-virtual {v4, v3}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v6

    .line 33
    .local v6, "sPackageManager":Ljava/lang/Object;
    const-string v7, "android.content.pm.IPackageManager"

    invoke-static {v7}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v7

    .line 34
    .local v7, "iPackageManagerInterface":Ljava/lang/Class;, "Ljava/lang/Class<*>;"
    nop

    .line 35
    invoke-virtual {v7}, Ljava/lang/Class;->getClassLoader()Ljava/lang/ClassLoader;

    move-result-object v8

    new-array v9, v5, [Ljava/lang/Class;

    aput-object v7, v9, v2

    new-instance v10, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;

    invoke-direct {v10, v6, p1, p2, v2}, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;-><init>(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;I)V

    .line 34
    invoke-static {v8, v9, v10}, Ljava/lang/reflect/Proxy;->newProxyInstance(Ljava/lang/ClassLoader;[Ljava/lang/Class;Ljava/lang/reflect/InvocationHandler;)Ljava/lang/Object;

    move-result-object v2

    .line 39
    .local v2, "proxy":Ljava/lang/Object;
    invoke-virtual {v4, v3, v2}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    .line 41
    invoke-virtual {p0}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v8

    .line 42
    .local v8, "pm":Landroid/content/pm/PackageManager;
    invoke-virtual {v8}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v9

    const-string v10, "mPM"

    invoke-virtual {v9, v10}, Ljava/lang/Class;->getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;

    move-result-object v9

    .line 43
    .local v9, "mPmField":Ljava/lang/reflect/Field;
    invoke-virtual {v9, v5}, Ljava/lang/reflect/Field;->setAccessible(Z)V

    .line 44
    invoke-virtual {v9, v8, v2}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 47
    .end local v0    # "activityThreadClass":Ljava/lang/Class;, "Ljava/lang/Class<*>;"
    .end local v1    # "currentActivityThreadMethod":Ljava/lang/reflect/Method;
    .end local v2    # "proxy":Ljava/lang/Object;
    .end local v3    # "currentActivityThread":Ljava/lang/Object;
    .end local v4    # "sPackageManagerField":Ljava/lang/reflect/Field;
    .end local v6    # "sPackageManager":Ljava/lang/Object;
    .end local v7    # "iPackageManagerInterface":Ljava/lang/Class;, "Ljava/lang/Class<*>;"
    .end local v8    # "pm":Landroid/content/pm/PackageManager;
    .end local v9    # "mPmField":Ljava/lang/reflect/Field;
    goto :goto_0

    .line 45
    :catch_0
    move-exception v0

    .line 46
    .local v0, "e":Ljava/lang/Exception;
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "hook pms error:"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v0}, Landroid/util/Log;->getStackTraceString(Ljava/lang/Throwable;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    const-string v2, "jw"

    invoke-static {v2, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 48
    .end local v0    # "e":Ljava/lang/Exception;
    :goto_0
    return-void
.end method
