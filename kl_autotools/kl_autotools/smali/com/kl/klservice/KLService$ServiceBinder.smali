.class public Lcom/kl/klservice/KLService$ServiceBinder;
.super Landroid/os/Binder;
.source "KLService.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/kl/klservice/KLService;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x1
    name = "ServiceBinder"
.end annotation


# instance fields
.field final synthetic this$0:Lcom/kl/klservice/KLService;


# direct methods
.method public constructor <init>(Lcom/kl/klservice/KLService;)V
    .locals 0
    .parameter

    .prologue
    .line 56
    iput-object p1, p0, Lcom/kl/klservice/KLService$ServiceBinder;->this$0:Lcom/kl/klservice/KLService;

    invoke-direct {p0}, Landroid/os/Binder;-><init>()V

    return-void
.end method


# virtual methods
.method public getService()Lcom/kl/klservice/KLService;
    .locals 1

    .prologue
    .line 58
    iget-object v0, p0, Lcom/kl/klservice/KLService$ServiceBinder;->this$0:Lcom/kl/klservice/KLService;

    return-object v0
.end method
