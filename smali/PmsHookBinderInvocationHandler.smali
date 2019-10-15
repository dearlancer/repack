.class public Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;
.super Ljava/lang/Object;
.source "PmsHookBinderInvocationHandler.java"

# interfaces
.implements Ljava/lang/reflect/InvocationHandler;


# instance fields
.field private appPkgName:Ljava/lang/String;

.field private base:Ljava/lang/Object;

.field private sign:Ljava/lang/String;


# direct methods
.method public constructor <init>(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;I)V
    .locals 3
    .param p1, "base"    # Ljava/lang/Object;
    .param p2, "sign"    # Ljava/lang/String;
    .param p3, "appPkgName"    # Ljava/lang/String;
    .param p4, "hashCode"    # I

    .line 23
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    .line 25
    :try_start_0
    iput-object p1, p0, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;->base:Ljava/lang/Object;

    .line 26
    iput-object p2, p0, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;->sign:Ljava/lang/String;

    .line 27
    iput-object p3, p0, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;->appPkgName:Ljava/lang/String;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 30
    goto :goto_0

    .line 28
    :catch_0
    move-exception v0

    .line 29
    .local v0, "e":Ljava/lang/Exception;
    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "error:"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {v0}, Landroid/util/Log;->getStackTraceString(Ljava/lang/Throwable;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    const-string v2, "hookPms"

    invoke-static {v2, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 31
    .end local v0    # "e":Ljava/lang/Exception;
    :goto_0
    return-void
.end method


# virtual methods
.method public invoke(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6
    .param p1, "proxy"    # Ljava/lang/Object;
    .param p2, "method"    # Ljava/lang/reflect/Method;
    .param p3, "args"    # [Ljava/lang/Object;
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/lang/Throwable;
        }
    .end annotation

    .line 35
    invoke-virtual {p2}, Ljava/lang/reflect/Method;->getName()Ljava/lang/String;

    move-result-object v0

    const-string v1, "hookPms"

    invoke-static {v1, v0}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 36
    invoke-virtual {p2}, Ljava/lang/reflect/Method;->getName()Ljava/lang/String;

    move-result-object v0

    const-string v1, "getPackageInfo"

    invoke-virtual {v1, v0}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_0

    .line 37
    const/4 v0, 0x0

    aget-object v1, p3, v0

    check-cast v1, Ljava/lang/String;

    .line 38
    .local v1, "pkgName":Ljava/lang/String;
    const/4 v2, 0x1

    aget-object v2, p3, v2

    check-cast v2, Ljava/lang/Integer;

    .line 39
    .local v2, "flag":Ljava/lang/Integer;
    invoke-virtual {v2}, Ljava/lang/Integer;->intValue()I

    move-result v3

    const/16 v4, 0x40

    if-ne v3, v4, :cond_0

    iget-object v3, p0, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;->appPkgName:Ljava/lang/String;

    invoke-virtual {v3, v1}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v3

    if-eqz v3, :cond_0

    .line 40
    new-instance v3, Landroid/content/pm/Signature;

    iget-object v4, p0, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;->sign:Ljava/lang/String;

    invoke-direct {v3, v4}, Landroid/content/pm/Signature;-><init>(Ljava/lang/String;)V

    .line 41
    .local v3, "signature":Landroid/content/pm/Signature;
    iget-object v4, p0, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;->base:Ljava/lang/Object;

    invoke-virtual {p2, v4, p3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/content/pm/PackageInfo;

    .line 42
    .local v4, "info":Landroid/content/pm/PackageInfo;
    iget-object v5, v4, Landroid/content/pm/PackageInfo;->signatures:[Landroid/content/pm/Signature;

    aput-object v3, v5, v0

    .line 43
    return-object v4

    .line 46
    .end local v1    # "pkgName":Ljava/lang/String;
    .end local v2    # "flag":Ljava/lang/Integer;
    .end local v3    # "signature":Landroid/content/pm/Signature;
    .end local v4    # "info":Landroid/content/pm/PackageInfo;
    :cond_0
    iget-object v0, p0, Lcn/wzc/hookpms/PmsHookBinderInvocationHandler;->base:Ljava/lang/Object;

    invoke-virtual {p2, v0, p3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    return-object v0
.end method
