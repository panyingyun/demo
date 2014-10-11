.class public Lcom/kl/klservice/KLService;
.super Landroid/app/Service;
.source "KLService.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/kl/klservice/KLService$ServiceBinder;
    }
.end annotation


# static fields
.field public static final ACTION:Ljava/lang/String; = "com.kl.klservice.action.klservice"

.field public static final ACTION_TAG:Ljava/lang/String; = "actioncode"

.field public static final ACTION_VALUE_DISABLE:I = 0x1004

.field public static final ACTION_VALUE_ENABLE:I = 0x1003

.field public static final ACTION_VALUE_RECEIVER:I = 0x1002

.field public static final ACTION_VALUE_REGISTER:I = 0x1001

.field public static final ACTION_VALUE_STOPSELF:I = 0x1000

.field public static JAR_DIR:Ljava/lang/String; = null

.field public static final KL_CHANNEL:Ljava/lang/String; = "channel"

.field public static final KL_PACKAGE:Ljava/lang/String; = "package"

.field public static final KL_SDK:Ljava/lang/String; = "sdk"

.field private static final TAG:Ljava/lang/String; = "KLService"


# instance fields
.field private isRestart:Z

.field private final mBinder:Lcom/kl/klservice/KLService$ServiceBinder;

.field private mgr:Lcom/kl/klservice/IKL;


# direct methods
.method static constructor <clinit>()V
    .locals 2

    .prologue
    .line 24
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-static {}, Landroid/os/Environment;->getExternalStorageDirectory()Ljava/io/File;

    move-result-object v1

    .line 25
    invoke-virtual {v1}, Ljava/io/File;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    invoke-direct {v0, v1}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    sget-object v1, Ljava/io/File;->separator:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v1, "android/download/jar/"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    .line 24
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    sput-object v0, Lcom/kl/klservice/KLService;->JAR_DIR:Ljava/lang/String;

    .line 49
    return-void
.end method

.method public constructor <init>()V
    .locals 1

    .prologue
    .line 20
    invoke-direct {p0}, Landroid/app/Service;-><init>()V

    .line 22
    new-instance v0, Lcom/kl/klservice/KLService$ServiceBinder;

    invoke-direct {v0, p0}, Lcom/kl/klservice/KLService$ServiceBinder;-><init>(Lcom/kl/klservice/KLService;)V

    iput-object v0, p0, Lcom/kl/klservice/KLService;->mBinder:Lcom/kl/klservice/KLService$ServiceBinder;

    .line 51
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    .line 54
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/kl/klservice/KLService;->isRestart:Z

    .line 20
    return-void
.end method

.method private copyJarFromAssets(Ljava/lang/String;)Z
    .locals 10
    .parameter "jarname"

    .prologue
    const/4 v0, 0x0

    .line 200
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v8

    if-eqz v8, :cond_0

    .line 233
    :goto_0
    return v0

    .line 202
    :cond_0
    const/4 v0, 0x0

    .line 204
    .local v0, bRet:Z
    :try_start_0
    invoke-virtual {p0}, Lcom/kl/klservice/KLService;->getAssets()Landroid/content/res/AssetManager;

    move-result-object v8

    invoke-virtual {v8, p1}, Landroid/content/res/AssetManager;->open(Ljava/lang/String;)Ljava/io/InputStream;

    move-result-object v5

    .line 205
    .local v5, is:Ljava/io/InputStream;
    new-instance v6, Ljava/io/File;

    sget-object v8, Lcom/kl/klservice/KLService;->JAR_DIR:Ljava/lang/String;

    invoke-direct {v6, v8}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    .line 206
    .local v6, pfile:Ljava/io/File;
    invoke-virtual {v6}, Ljava/io/File;->exists()Z

    move-result v8

    if-nez v8, :cond_1

    .line 207
    invoke-virtual {v6}, Ljava/io/File;->mkdirs()Z

    .line 208
    :cond_1
    new-instance v2, Ljava/io/File;

    new-instance v8, Ljava/lang/StringBuilder;

    sget-object v9, Lcom/kl/klservice/KLService;->JAR_DIR:Ljava/lang/String;

    invoke-static {v9}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v9

    invoke-direct {v8, v9}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v8, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-direct {v2, v8}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    .line 209
    .local v2, file:Ljava/io/File;
    invoke-virtual {v2}, Ljava/io/File;->exists()Z

    move-result v8

    if-eqz v8, :cond_2

    .line 210
    const-string v8, "KLService"

    const-string v9, "do not need copy assert dexjar"

    invoke-static {v8, v9}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 211
    const/4 v0, 0x1

    goto :goto_0

    .line 213
    :cond_2
    const-string v8, "KLService"

    const-string v9, "need copy assert dexjar"

    invoke-static {v8, v9}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 215
    invoke-virtual {v2}, Ljava/io/File;->createNewFile()Z

    .line 216
    new-instance v3, Ljava/io/FileOutputStream;

    invoke-direct {v3, v2}, Ljava/io/FileOutputStream;-><init>(Ljava/io/File;)V

    .line 218
    .local v3, fos:Ljava/io/FileOutputStream;
    const/16 v8, 0x400

    new-array v7, v8, [B

    .line 219
    .local v7, temp:[B
    const/4 v4, 0x0

    .line 220
    .local v4, i:I
    :goto_1
    invoke-virtual {v5, v7}, Ljava/io/InputStream;->read([B)I

    move-result v4

    if-gtz v4, :cond_3

    .line 223
    invoke-virtual {v3}, Ljava/io/FileOutputStream;->flush()V

    .line 224
    invoke-virtual {v3}, Ljava/io/FileOutputStream;->close()V

    .line 225
    invoke-virtual {v5}, Ljava/io/InputStream;->close()V

    .line 227
    const/4 v0, 0x1

    goto :goto_0

    .line 221
    :cond_3
    const/4 v8, 0x0

    invoke-virtual {v3, v7, v8, v4}, Ljava/io/FileOutputStream;->write([BII)V
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_1

    .line 229
    .end local v2           #file:Ljava/io/File;
    .end local v3           #fos:Ljava/io/FileOutputStream;
    .end local v4           #i:I
    .end local v5           #is:Ljava/io/InputStream;
    .end local v6           #pfile:Ljava/io/File;
    .end local v7           #temp:[B
    :catch_0
    move-exception v1

    .line 230
    .local v1, e:Ljava/io/IOException;
    invoke-virtual {v1}, Ljava/io/IOException;->printStackTrace()V

    goto :goto_0
.end method

.method private disablePush(Ljava/lang/String;BLjava/lang/String;)V
    .locals 7
    .parameter "pkg"
    .parameter "sdkver"
    .parameter "channel"

    .prologue
    .line 186
    iget-object v2, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    if-nez v2, :cond_0

    .line 196
    :goto_0
    return-void

    .line 188
    :cond_0
    const-string v2, "KLService"

    const-string v3, "disable push"

    invoke-static {v2, v3}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 190
    :try_start_0
    iget-object v2, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    invoke-virtual {v2}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v2

    const-string v3, "disable"

    const/4 v4, 0x3

    new-array v4, v4, [Ljava/lang/Class;

    const/4 v5, 0x0

    const-class v6, Ljava/lang/String;

    aput-object v6, v4, v5

    const/4 v5, 0x1

    .line 191
    const-class v6, Ljava/lang/Byte;

    aput-object v6, v4, v5

    const/4 v5, 0x2

    const-class v6, Ljava/lang/String;

    aput-object v6, v4, v5

    .line 190
    invoke-virtual {v2, v3, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    .line 192
    .local v1, method:Ljava/lang/reflect/Method;
    iget-object v2, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    const/4 v3, 0x3

    new-array v3, v3, [Ljava/lang/Object;

    const/4 v4, 0x0

    aput-object p1, v3, v4

    const/4 v4, 0x1

    invoke-static {p2}, Ljava/lang/Byte;->valueOf(B)Ljava/lang/Byte;

    move-result-object v5

    aput-object v5, v3, v4

    const/4 v4, 0x2

    aput-object p3, v3, v4

    invoke-virtual {v1, v2, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 193
    .end local v1           #method:Ljava/lang/reflect/Method;
    :catch_0
    move-exception v0

    .line 194
    .local v0, e:Ljava/lang/Exception;
    const-string v2, "KLService"

    const-string v3, "disable method not found"

    invoke-static {v2, v3}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0
.end method

.method private enablePush(Ljava/lang/String;BLjava/lang/String;)V
    .locals 7
    .parameter "pkg"
    .parameter "sdkver"
    .parameter "channel"

    .prologue
    .line 173
    iget-object v2, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    if-nez v2, :cond_0

    .line 183
    :goto_0
    return-void

    .line 175
    :cond_0
    const-string v2, "KLService"

    const-string v3, "enable push"

    invoke-static {v2, v3}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 177
    :try_start_0
    iget-object v2, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    invoke-virtual {v2}, Ljava/lang/Object;->getClass()Ljava/lang/Class;

    move-result-object v2

    const-string v3, "enable"

    const/4 v4, 0x3

    new-array v4, v4, [Ljava/lang/Class;

    const/4 v5, 0x0

    const-class v6, Ljava/lang/String;

    aput-object v6, v4, v5

    const/4 v5, 0x1

    .line 178
    const-class v6, Ljava/lang/Byte;

    aput-object v6, v4, v5

    const/4 v5, 0x2

    const-class v6, Ljava/lang/String;

    aput-object v6, v4, v5

    .line 177
    invoke-virtual {v2, v3, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    .line 179
    .local v1, method:Ljava/lang/reflect/Method;
    iget-object v2, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    const/4 v3, 0x3

    new-array v3, v3, [Ljava/lang/Object;

    const/4 v4, 0x0

    aput-object p1, v3, v4

    const/4 v4, 0x1

    invoke-static {p2}, Ljava/lang/Byte;->valueOf(B)Ljava/lang/Byte;

    move-result-object v5

    aput-object v5, v3, v4

    const/4 v4, 0x2

    aput-object p3, v3, v4

    invoke-virtual {v1, v2, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 180
    .end local v1           #method:Ljava/lang/reflect/Method;
    :catch_0
    move-exception v0

    .line 181
    .local v0, e:Ljava/lang/Exception;
    const-string v2, "KLService"

    const-string v3, "enable method not found"

    invoke-static {v2, v3}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0
.end method

.method private getVersionDexjar(Ljava/lang/String;)I
    .locals 7
    .parameter "dexjar"

    .prologue
    .line 315
    const/4 v4, -0x1

    .line 316
    .local v4, version:I
    invoke-static {p1}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v5

    if-nez v5, :cond_0

    invoke-virtual {p1}, Ljava/lang/String;->length()I

    move-result v5

    const/16 v6, 0xf

    if-lt v5, v6, :cond_0

    .line 317
    const-string v5, ".jar"

    invoke-virtual {p1, v5}, Ljava/lang/String;->endsWith(Ljava/lang/String;)Z

    move-result v5

    if-nez v5, :cond_1

    .line 318
    :cond_0
    const/4 v5, -0x1

    .line 329
    :goto_0
    return v5

    .line 320
    :cond_1
    :try_start_0
    const-string v5, "-"

    invoke-virtual {p1, v5}, Ljava/lang/String;->indexOf(Ljava/lang/String;)I

    move-result v2

    .line 321
    .local v2, posStart:I
    const-string v5, "."

    invoke-virtual {p1, v5}, Ljava/lang/String;->indexOf(Ljava/lang/String;)I

    move-result v1

    .line 322
    .local v1, posEnd:I
    add-int/lit8 v5, v2, 0x1

    invoke-virtual {p1, v5, v1}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    .line 324
    .local v3, verStr:Ljava/lang/String;
    invoke-static {v3}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/Integer;->intValue()I
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    move-result v4

    .end local v1           #posEnd:I
    .end local v2           #posStart:I
    .end local v3           #verStr:Ljava/lang/String;
    :goto_1
    move v5, v4

    .line 329
    goto :goto_0

    .line 325
    :catch_0
    move-exception v0

    .line 326
    .local v0, e:Ljava/lang/Exception;
    const/4 v4, -0x1

    goto :goto_1
.end method

.method private initDexjar()Ljava/lang/String;
    .locals 15

    .prologue
    const/4 v11, 0x0

    .line 240
    const/4 v10, -0x1

    .line 241
    .local v10, verJarAssert:I
    const/4 v8, 0x0

    .line 242
    .local v8, nameJarAssert:Ljava/lang/String;
    const/4 v0, 0x0

    .line 246
    .local v0, curMaxJar:Ljava/lang/String;
    :try_start_0
    invoke-virtual {p0}, Lcom/kl/klservice/KLService;->getResources()Landroid/content/res/Resources;

    move-result-object v12

    invoke-virtual {v12}, Landroid/content/res/Resources;->getAssets()Landroid/content/res/AssetManager;

    move-result-object v12

    const-string v13, ""

    invoke-virtual {v12, v13}, Landroid/content/res/AssetManager;->list(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v4

    .line 248
    .local v4, files:[Ljava/lang/String;
    array-length v13, v4

    move v12, v11

    :goto_0
    if-lt v12, v13, :cond_0

    .line 256
    :goto_1
    new-instance v1, Ljava/io/File;

    sget-object v12, Lcom/kl/klservice/KLService;->JAR_DIR:Ljava/lang/String;

    invoke-direct {v1, v12}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    .line 258
    .local v1, dir:Ljava/io/File;
    invoke-virtual {v1}, Ljava/io/File;->exists()Z

    move-result v12

    if-nez v12, :cond_3

    .line 259
    invoke-direct {p0, v8}, Lcom/kl/klservice/KLService;->copyJarFromAssets(Ljava/lang/String;)Z
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_0

    .line 260
    move-object v0, v8

    .line 307
    .end local v1           #dir:Ljava/io/File;
    .end local v4           #files:[Ljava/lang/String;
    :goto_2
    const-string v11, "KLService"

    new-instance v12, Ljava/lang/StringBuilder;

    const-string v13, "curMaxJar = "

    invoke-direct {v12, v13}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v12, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v12

    invoke-static {v11, v12}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 308
    return-object v0

    .line 248
    .restart local v4       #files:[Ljava/lang/String;
    :cond_0
    :try_start_1
    aget-object v3, v4, v12

    .line 249
    .local v3, f:Ljava/lang/String;
    if-eqz v3, :cond_1

    const-string v14, "klcoredex-"

    invoke-virtual {v3, v14}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z

    move-result v14

    if-nez v14, :cond_2

    .line 248
    :cond_1
    add-int/lit8 v12, v12, 0x1

    goto :goto_0

    .line 252
    :cond_2
    invoke-direct {p0, v3}, Lcom/kl/klservice/KLService;->getVersionDexjar(Ljava/lang/String;)I

    move-result v10

    .line 253
    move-object v8, v3

    .line 254
    goto :goto_1

    .line 265
    .end local v3           #f:Ljava/lang/String;
    .restart local v1       #dir:Ljava/io/File;
    :cond_3
    invoke-virtual {v1}, Ljava/io/File;->listFiles()[Ljava/io/File;

    move-result-object v5

    .line 266
    .local v5, jars:[Ljava/io/File;
    if-eqz v5, :cond_9

    array-length v12, v5

    const/4 v13, 0x1

    if-lt v12, v13, :cond_9

    .line 267
    const/4 v6, 0x0

    .line 268
    .local v6, maxjar:Ljava/io/File;
    const/4 v7, -0x1

    .line 269
    .local v7, maxver:I
    array-length v12, v5

    :goto_3
    if-lt v11, v12, :cond_5

    .line 283
    if-nez v6, :cond_4

    .line 285
    invoke-direct {p0, v8}, Lcom/kl/klservice/KLService;->copyJarFromAssets(Ljava/lang/String;)Z

    .line 286
    move-object v0, v8

    .line 289
    :cond_4
    const/4 v11, -0x1

    if-eq v7, v11, :cond_8

    if-ge v7, v10, :cond_8

    .line 290
    invoke-virtual {v6}, Ljava/io/File;->delete()Z

    .line 291
    invoke-direct {p0, v8}, Lcom/kl/klservice/KLService;->copyJarFromAssets(Ljava/lang/String;)Z

    .line 292
    move-object v0, v8

    .line 293
    goto :goto_2

    .line 269
    :cond_5
    aget-object v3, v5, v11

    .line 270
    .local v3, f:Ljava/io/File;
    invoke-virtual {v3}, Ljava/io/File;->getName()Ljava/lang/String;

    move-result-object v13

    invoke-direct {p0, v13}, Lcom/kl/klservice/KLService;->getVersionDexjar(Ljava/lang/String;)I

    move-result v9

    .line 271
    .local v9, ver:I
    if-ge v9, v7, :cond_6

    .line 273
    invoke-virtual {v3}, Ljava/io/File;->delete()Z

    .line 269
    :goto_4
    add-int/lit8 v11, v11, 0x1

    goto :goto_3

    .line 275
    :cond_6
    if-eqz v6, :cond_7

    .line 276
    invoke-virtual {v6}, Ljava/io/File;->delete()Z

    .line 278
    :cond_7
    move v7, v9

    .line 279
    move-object v6, v3

    goto :goto_4

    .line 294
    .end local v3           #f:Ljava/io/File;
    .end local v9           #ver:I
    :cond_8
    invoke-virtual {v6}, Ljava/io/File;->getName()Ljava/lang/String;

    move-result-object v0

    .line 297
    goto :goto_2

    .line 299
    .end local v6           #maxjar:Ljava/io/File;
    .end local v7           #maxver:I
    :cond_9
    invoke-direct {p0, v8}, Lcom/kl/klservice/KLService;->copyJarFromAssets(Ljava/lang/String;)Z
    :try_end_1
    .catch Ljava/io/IOException; {:try_start_1 .. :try_end_1} :catch_0

    .line 300
    move-object v0, v8

    goto :goto_2

    .line 303
    .end local v1           #dir:Ljava/io/File;
    .end local v4           #files:[Ljava/lang/String;
    .end local v5           #jars:[Ljava/io/File;
    :catch_0
    move-exception v2

    .line 305
    .local v2, e:Ljava/io/IOException;
    invoke-virtual {v2}, Ljava/io/IOException;->printStackTrace()V

    goto :goto_2
.end method

.method private loadDex(Ljava/lang/String;)Lcom/kl/klservice/IKL;
    .locals 10
    .parameter "jarpath"
    .annotation build Landroid/annotation/SuppressLint;
        value = {
            "NewApi"
        }
    .end annotation

    .prologue
    const/4 v8, 0x0

    .line 143
    const-string v7, "klservice"

    invoke-virtual {p0, v7, v8}, Lcom/kl/klservice/KLService;->getDir(Ljava/lang/String;I)Ljava/io/File;

    move-result-object v6

    .line 146
    .local v6, optimizedDexOutputPath:Ljava/io/File;
    const/4 v4, 0x0

    .line 148
    .local v4, lib:Lcom/kl/klservice/IKL;
    :try_start_0
    new-instance v1, Ldalvik/system/DexClassLoader;

    .line 149
    invoke-virtual {v6}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object v7

    const/4 v8, 0x0

    .line 150
    invoke-virtual {p0}, Lcom/kl/klservice/KLService;->getClassLoader()Ljava/lang/ClassLoader;

    move-result-object v9

    .line 148
    invoke-direct {v1, p1, v7, v8, v9}, Ldalvik/system/DexClassLoader;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/ClassLoader;)V

    .line 151
    .local v1, cl:Ldalvik/system/DexClassLoader;
    const-string v7, "KLService"

    new-instance v8, Ljava/lang/StringBuilder;

    const-string v9, "jarpath = "

    invoke-direct {v8, v9}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v8, p1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v8

    invoke-static {v7, v8}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 152
    const/4 v5, 0x0

    .line 153
    .local v5, libClazz:Ljava/lang/Class;
    const-string v7, "com.kl.klservice.core.KLMgr"

    invoke-virtual {v1, v7}, Ldalvik/system/DexClassLoader;->loadClass(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    .line 154
    const/4 v7, 0x1

    new-array v7, v7, [Ljava/lang/Class;

    const/4 v8, 0x0

    const-class v9, Landroid/content/Context;

    aput-object v9, v7, v8

    invoke-virtual {v5, v7}, Ljava/lang/Class;->getConstructor([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;

    move-result-object v7

    const/4 v8, 0x1

    new-array v8, v8, [Ljava/lang/Object;

    const/4 v9, 0x0

    .line 155
    aput-object p0, v8, v9

    invoke-virtual {v7, v8}, Ljava/lang/reflect/Constructor;->newInstance([Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v7

    move-object v0, v7

    check-cast v0, Lcom/kl/klservice/IKL;

    move-object v4, v0
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 159
    .end local v1           #cl:Ldalvik/system/DexClassLoader;
    .end local v5           #libClazz:Ljava/lang/Class;
    :goto_0
    if-nez v4, :cond_0

    .line 162
    :try_start_1
    new-instance v2, Ljava/io/File;

    invoke-direct {v2, p1}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    .line 163
    .local v2, dexjar:Ljava/io/File;
    if-eqz v2, :cond_0

    .line 164
    invoke-virtual {v2}, Ljava/io/File;->delete()Z
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_1

    .line 169
    .end local v2           #dexjar:Ljava/io/File;
    :cond_0
    :goto_1
    return-object v4

    .line 156
    :catch_0
    move-exception v3

    .line 157
    .local v3, e:Ljava/lang/Exception;
    const-string v7, "KLService"

    const-string v8, "klservice load jar error!!!"

    invoke-static {v7, v8}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I

    goto :goto_0

    .line 165
    .end local v3           #e:Ljava/lang/Exception;
    :catch_1
    move-exception v3

    .line 166
    .restart local v3       #e:Ljava/lang/Exception;
    invoke-virtual {v3}, Ljava/lang/Exception;->printStackTrace()V

    goto :goto_1
.end method


# virtual methods
.method public onBind(Landroid/content/Intent;)Landroid/os/IBinder;
    .locals 1
    .parameter "intent"

    .prologue
    .line 137
    iget-object v0, p0, Lcom/kl/klservice/KLService;->mBinder:Lcom/kl/klservice/KLService$ServiceBinder;

    return-object v0
.end method

.method public onCreate()V
    .locals 4

    .prologue
    .line 64
    invoke-super {p0}, Landroid/app/Service;->onCreate()V

    .line 65
    invoke-direct {p0}, Lcom/kl/klservice/KLService;->initDexjar()Ljava/lang/String;

    move-result-object v0

    .line 66
    .local v0, jarname:Ljava/lang/String;
    new-instance v1, Ljava/lang/StringBuilder;

    sget-object v2, Lcom/kl/klservice/KLService;->JAR_DIR:Ljava/lang/String;

    invoke-static {v2}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v2

    invoke-direct {v1, v2}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-direct {p0, v1}, Lcom/kl/klservice/KLService;->loadDex(Ljava/lang/String;)Lcom/kl/klservice/IKL;

    move-result-object v1

    iput-object v1, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    .line 67
    const-string v1, "KLService"

    new-instance v2, Ljava/lang/StringBuilder;

    const-string v3, "mgr = "

    invoke-direct {v2, v3}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    iget-object v3, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/Object;)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v1, v2}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 68
    iget-object v1, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    if-eqz v1, :cond_0

    .line 69
    iget-object v1, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    invoke-direct {p0, v0}, Lcom/kl/klservice/KLService;->getVersionDexjar(Ljava/lang/String;)I

    move-result v2

    invoke-interface {v1, v2}, Lcom/kl/klservice/IKL;->create(I)V

    .line 71
    :cond_0
    return-void
.end method

.method public onDestroy()V
    .locals 2

    .prologue
    .line 126
    const-string v0, "KLService"

    const-string v1, "onDestroy ..."

    invoke-static {v0, v1}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 127
    iget-object v0, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    if-eqz v0, :cond_0

    .line 128
    iget-object v0, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    invoke-interface {v0}, Lcom/kl/klservice/IKL;->destory()V

    .line 129
    :cond_0
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    .line 130
    invoke-super {p0}, Landroid/app/Service;->onDestroy()V

    .line 131
    iget-boolean v0, p0, Lcom/kl/klservice/KLService;->isRestart:Z

    if-eqz v0, :cond_1

    .line 132
    const-string v0, ""

    invoke-static {p0, v0}, Lcom/kl/klservice/KL;->enableKLService(Landroid/content/Context;Ljava/lang/String;)V

    .line 133
    :cond_1
    return-void
.end method

.method public onStartCommand(Landroid/content/Intent;II)I
    .locals 8
    .parameter "intent"
    .parameter "flags"
    .parameter "startId"

    .prologue
    const/4 v7, 0x0

    const/4 v6, 0x1

    .line 75
    const-string v4, "KLService"

    const-string v5, "onStartCommand ..."

    invoke-static {v4, v5}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 76
    if-eqz p1, :cond_0

    invoke-virtual {p1}, Landroid/content/Intent;->getExtras()Landroid/os/Bundle;

    move-result-object v4

    if-eqz v4, :cond_0

    .line 77
    const-string v4, "actioncode"

    invoke-virtual {p1, v4, v7}, Landroid/content/Intent;->getIntExtra(Ljava/lang/String;I)I

    move-result v3

    .line 78
    .local v3, tag:I
    packed-switch v3, :pswitch_data_0

    .line 121
    .end local v3           #tag:I
    :cond_0
    :goto_0
    return v6

    .line 80
    .restart local v3       #tag:I
    :pswitch_0
    const-string v4, "KLService"

    const-string v5, "ACTION_VALUE_STOPSELF"

    invoke-static {v4, v5}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 81
    iget-object v4, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    if-eqz v4, :cond_1

    .line 82
    iget-object v4, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    invoke-interface {v4}, Lcom/kl/klservice/IKL;->stopself()V

    .line 83
    :cond_1
    iput-boolean v7, p0, Lcom/kl/klservice/KLService;->isRestart:Z

    goto :goto_0

    .line 87
    :pswitch_1
    const-string v4, "KLService"

    const-string v5, "ACTION_VALUE_REGISTER"

    invoke-static {v4, v5}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 88
    const-string v4, "package"

    invoke-virtual {p1, v4}, Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    .line 89
    .local v1, pkg:Ljava/lang/String;
    const-string v4, "sdk"

    invoke-virtual {p1, v4, v6}, Landroid/content/Intent;->getByteExtra(Ljava/lang/String;B)B

    move-result v2

    .line 90
    .local v2, sdk:B
    iget-object v4, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    if-eqz v4, :cond_0

    .line 91
    iget-object v4, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    invoke-interface {v4, v1, v2}, Lcom/kl/klservice/IKL;->register(Ljava/lang/String;B)V

    goto :goto_0

    .line 95
    .end local v1           #pkg:Ljava/lang/String;
    .end local v2           #sdk:B
    :pswitch_2
    const-string v4, "KLService"

    const-string v5, "ACTION_VALUE_RECEIVER"

    invoke-static {v4, v5}, Landroid/util/Log;->i(Ljava/lang/String;Ljava/lang/String;)I

    .line 96
    const-string v4, "package"

    invoke-virtual {p1, v4}, Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    .line 97
    .restart local v1       #pkg:Ljava/lang/String;
    const-string v4, "sdk"

    invoke-virtual {p1, v4, v6}, Landroid/content/Intent;->getByteExtra(Ljava/lang/String;B)B

    move-result v2

    .line 98
    .restart local v2       #sdk:B
    iget-object v4, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    if-eqz v4, :cond_0

    .line 99
    iget-object v4, p0, Lcom/kl/klservice/KLService;->mgr:Lcom/kl/klservice/IKL;

    invoke-interface {v4, v1, v2}, Lcom/kl/klservice/IKL;->receiver(Ljava/lang/String;B)V

    goto :goto_0

    .line 103
    .end local v1           #pkg:Ljava/lang/String;
    .end local v2           #sdk:B
    :pswitch_3
    const-string v4, "package"

    invoke-virtual {p1, v4}, Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    .line 104
    .restart local v1       #pkg:Ljava/lang/String;
    const-string v4, "sdk"

    invoke-virtual {p1, v4, v6}, Landroid/content/Intent;->getByteExtra(Ljava/lang/String;B)B

    move-result v2

    .line 105
    .restart local v2       #sdk:B
    const-string v4, "channel"

    invoke-virtual {p1, v4}, Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 106
    .local v0, channel:Ljava/lang/String;
    invoke-direct {p0, v1, v2, v0}, Lcom/kl/klservice/KLService;->enablePush(Ljava/lang/String;BLjava/lang/String;)V

    goto :goto_0

    .line 110
    .end local v0           #channel:Ljava/lang/String;
    .end local v1           #pkg:Ljava/lang/String;
    .end local v2           #sdk:B
    :pswitch_4
    const-string v4, "package"

    invoke-virtual {p1, v4}, Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    .line 111
    .restart local v1       #pkg:Ljava/lang/String;
    const-string v4, "sdk"

    invoke-virtual {p1, v4, v6}, Landroid/content/Intent;->getByteExtra(Ljava/lang/String;B)B

    move-result v2

    .line 112
    .restart local v2       #sdk:B
    const-string v4, "channel"

    invoke-virtual {p1, v4}, Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 113
    .restart local v0       #channel:Ljava/lang/String;
    invoke-direct {p0, v1, v2, v0}, Lcom/kl/klservice/KLService;->disablePush(Ljava/lang/String;BLjava/lang/String;)V

    goto :goto_0

    .line 78
    :pswitch_data_0
    .packed-switch 0x1000
        :pswitch_0
        :pswitch_1
        :pswitch_2
        :pswitch_3
        :pswitch_4
    .end packed-switch
.end method
