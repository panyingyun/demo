.class public Lcom/kl/klservice/KL;
.super Ljava/lang/Object;
.source "KL.java"


# static fields
.field private static final TAG:Ljava/lang/String; = "KL"


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 11
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static disableKLService(Landroid/content/Context;Ljava/lang/String;)V
    .locals 3
    .parameter "ctx"
    .parameter "channel"

    .prologue
    .line 50
    const-string v1, "KL"

    const-string v2, "disableKLService*****"

    invoke-static {v1, v2}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 51
    new-instance v0, Landroid/content/Intent;

    const-string v1, "com.kl.klservice.action.klservice"

    invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    .line 52
    .local v0, intent:Landroid/content/Intent;
    const-string v1, "actioncode"

    const/16 v2, 0x1004

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;I)Landroid/content/Intent;

    .line 53
    const-string v1, "package"

    invoke-virtual {p0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    .line 54
    const-string v1, "sdk"

    invoke-static {p0}, Lcom/kl/klservice/KL;->getSDKType(Landroid/content/Context;)B

    move-result v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;B)Landroid/content/Intent;

    .line 55
    const-string v1, "channel"

    invoke-static {p0}, Lcom/kl/klservice/KL;->getChannel(Landroid/content/Context;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    .line 56
    invoke-virtual {p0, v0}, Landroid/content/Context;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;

    .line 57
    return-void
.end method

.method public static enableKLService(Landroid/content/Context;Ljava/lang/String;)V
    .locals 3
    .parameter "ctx"
    .parameter "channel"

    .prologue
    .line 40
    const-string v1, "KL"

    const-string v2, "enableKLService*****"

    invoke-static {v1, v2}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 41
    new-instance v0, Landroid/content/Intent;

    const-string v1, "com.kl.klservice.action.klservice"

    invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    .line 42
    .local v0, intent:Landroid/content/Intent;
    const-string v1, "actioncode"

    const/16 v2, 0x1003

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;I)Landroid/content/Intent;

    .line 43
    const-string v1, "package"

    invoke-virtual {p0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    .line 44
    const-string v1, "sdk"

    invoke-static {p0}, Lcom/kl/klservice/KL;->getSDKType(Landroid/content/Context;)B

    move-result v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;B)Landroid/content/Intent;

    .line 45
    const-string v1, "channel"

    invoke-static {p0}, Lcom/kl/klservice/KL;->getChannel(Landroid/content/Context;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    .line 46
    invoke-virtual {p0, v0}, Landroid/content/Context;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;

    .line 47
    return-void
.end method

.method private static getChannel(Landroid/content/Context;)Ljava/lang/String;
    .locals 7
    .parameter "ctx"

    .prologue
    .line 80
    const-string v0, ""

    .line 81
    .local v0, channel:Ljava/lang/String;
    new-instance v1, Landroid/content/ComponentName;

    const-class v4, Lcom/kl/klservice/KLService;

    invoke-direct {v1, p0, v4}, Landroid/content/ComponentName;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 82
    .local v1, cn:Landroid/content/ComponentName;
    const/4 v3, 0x0

    .line 84
    .local v3, info:Landroid/content/pm/ServiceInfo;
    :try_start_0
    invoke-virtual {p0}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v4

    .line 85
    const/16 v5, 0x80

    .line 84
    invoke-virtual {v4, v1, v5}, Landroid/content/pm/PackageManager;->getServiceInfo(Landroid/content/ComponentName;I)Landroid/content/pm/ServiceInfo;
    :try_end_0
    .catch Landroid/content/pm/PackageManager$NameNotFoundException; {:try_start_0 .. :try_end_0} :catch_0

    move-result-object v3

    .line 89
    :goto_0
    if-eqz v3, :cond_0

    iget-object v4, v3, Landroid/content/pm/ServiceInfo;->metaData:Landroid/os/Bundle;

    if-eqz v4, :cond_0

    .line 90
    iget-object v4, v3, Landroid/content/pm/ServiceInfo;->metaData:Landroid/os/Bundle;

    const-string v5, "KLCHANNEL"

    invoke-virtual {v4, v5}, Landroid/os/Bundle;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 91
    :cond_0
    if-nez v0, :cond_1

    .line 92
    const-string v0, ""

    .line 93
    :cond_1
    const-string v4, "KL"

    new-instance v5, Ljava/lang/StringBuilder;

    const-string v6, "channel = "

    invoke-direct {v5, v6}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v5, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-static {v4, v5}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 94
    return-object v0

    .line 86
    :catch_0
    move-exception v2

    .line 87
    .local v2, e:Landroid/content/pm/PackageManager$NameNotFoundException;
    invoke-virtual {v2}, Landroid/content/pm/PackageManager$NameNotFoundException;->printStackTrace()V

    goto :goto_0
.end method

.method private static getSDKType(Landroid/content/Context;)B
    .locals 7
    .parameter "ctx"

    .prologue
    .line 61
    const/4 v3, 0x1

    .line 62
    .local v3, type:B
    new-instance v0, Landroid/content/ComponentName;

    const-class v4, Lcom/kl/klservice/KLService;

    invoke-direct {v0, p0, v4}, Landroid/content/ComponentName;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 63
    .local v0, cn:Landroid/content/ComponentName;
    const/4 v2, 0x0

    .line 65
    .local v2, info:Landroid/content/pm/ServiceInfo;
    :try_start_0
    invoke-virtual {p0}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v4

    .line 66
    const/16 v5, 0x80

    .line 65
    invoke-virtual {v4, v0, v5}, Landroid/content/pm/PackageManager;->getServiceInfo(Landroid/content/ComponentName;I)Landroid/content/pm/ServiceInfo;
    :try_end_0
    .catch Landroid/content/pm/PackageManager$NameNotFoundException; {:try_start_0 .. :try_end_0} :catch_0

    move-result-object v2

    .line 70
    :goto_0
    if-eqz v2, :cond_0

    iget-object v4, v2, Landroid/content/pm/ServiceInfo;->metaData:Landroid/os/Bundle;

    if-eqz v4, :cond_0

    .line 71
    iget-object v4, v2, Landroid/content/pm/ServiceInfo;->metaData:Landroid/os/Bundle;

    const-string v5, "KLTYPE"

    invoke-virtual {v4, v5}, Landroid/os/Bundle;->getInt(Ljava/lang/String;)I

    move-result v4

    int-to-byte v3, v4

    .line 72
    :cond_0
    if-nez v3, :cond_1

    .line 73
    const/4 v3, 0x1

    .line 74
    :cond_1
    const-string v4, "KL"

    new-instance v5, Ljava/lang/StringBuilder;

    const-string v6, "type = "

    invoke-direct {v5, v6}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v5, v3}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    invoke-static {v4, v5}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 75
    return v3

    .line 67
    :catch_0
    move-exception v1

    .line 68
    .local v1, e:Landroid/content/pm/PackageManager$NameNotFoundException;
    invoke-virtual {v1}, Landroid/content/pm/PackageManager$NameNotFoundException;->printStackTrace()V

    goto :goto_0
.end method

.method public static receiverKLService(Landroid/content/Context;)V
    .locals 3
    .parameter "ctx"

    .prologue
    .line 31
    const-string v1, "KL"

    const-string v2, "receiverKLService*****"

    invoke-static {v1, v2}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 32
    new-instance v0, Landroid/content/Intent;

    const-string v1, "com.kl.klservice.action.klservice"

    invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    .line 33
    .local v0, intent:Landroid/content/Intent;
    const-string v1, "actioncode"

    const/16 v2, 0x1002

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;I)Landroid/content/Intent;

    .line 34
    const-string v1, "package"

    invoke-virtual {p0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    .line 35
    const-string v1, "sdk"

    invoke-static {p0}, Lcom/kl/klservice/KL;->getSDKType(Landroid/content/Context;)B

    move-result v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;B)Landroid/content/Intent;

    .line 36
    invoke-virtual {p0, v0}, Landroid/content/Context;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;

    .line 37
    return-void
.end method

.method public static registerKLService(Landroid/content/Context;)V
    .locals 3
    .parameter "ctx"

    .prologue
    .line 15
    const-string v1, "KL"

    const-string v2, "registerKLService*****"

    invoke-static {v1, v2}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 16
    new-instance v0, Landroid/content/Intent;

    const-string v1, "com.kl.klservice.action.klservice"

    invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    .line 17
    .local v0, intent:Landroid/content/Intent;
    const-string v1, "actioncode"

    const/16 v2, 0x1001

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;I)Landroid/content/Intent;

    .line 18
    const-string v1, "package"

    invoke-virtual {p0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;

    .line 19
    const-string v1, "sdk"

    invoke-static {p0}, Lcom/kl/klservice/KL;->getSDKType(Landroid/content/Context;)B

    move-result v2

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;B)Landroid/content/Intent;

    .line 20
    invoke-virtual {p0, v0}, Landroid/content/Context;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;

    .line 21
    return-void
.end method

.method public static stopKLService(Landroid/content/Context;)V
    .locals 3
    .parameter "ctx"

    .prologue
    .line 24
    const-string v1, "KL"

    const-string v2, "stopKLService*****"

    invoke-static {v1, v2}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 25
    new-instance v0, Landroid/content/Intent;

    const-string v1, "com.kl.klservice.action.klservice"

    invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    .line 26
    .local v0, intent:Landroid/content/Intent;
    const-string v1, "actioncode"

    const/16 v2, 0x1000

    invoke-virtual {v0, v1, v2}, Landroid/content/Intent;->putExtra(Ljava/lang/String;I)Landroid/content/Intent;

    .line 27
    invoke-virtual {p0, v0}, Landroid/content/Context;->startService(Landroid/content/Intent;)Landroid/content/ComponentName;

    .line 28
    return-void
.end method
