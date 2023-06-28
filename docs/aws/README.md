# AWS

Amazon Web Services (AWS) is a major cloud provider, offering services used by companies worldwide to host web applications.

Here are some major AWS services (some of which are described below):

- [Amazon Elastic Cloud Compute (EC2)](#using-ec2): virtual machines available for rent
    - Amazon Linux 2 is the official Linux distribution for AWS
- [Amazon Elastic Container Service (ECS)](#using-ecs): AWS-managed Docker container service that runs containers within a cluster of EC2 instances
    - Related services include Amazon Elastic Container Registry (ECR) which is similar to Docker Hub and Amazon Elastic Kubernetes Service (EKS) which is an AWS-managed Kubernetes service
- [Amazon Simple Storage Service (S3)](#using-s3): storage "buckets" used to persist data
    - They do not have to be associated with an EC2 instance (serverless)
    - Alternative services include high-performance Amazon Elastic Block Storage (EBS) for individual EC2 instances and dynamically-sizing [Amazon Elastic File System (EFS)](#using-efs) for multiple EC2 instances
- Amazon Relational Database Service (RDS): AWS-managed distributed SQL database service
    - Amazon Aurora is the official RDBMS for AWS (compatible with PostgreSQL or MySQL)
- AWS Lambda: serverless computing service
    - Just upload code to Lambda without needing to host it on an EC2 instance, since it will only run when triggered (good for short-running functions that would need their own EC2 instances when running)

Software development services:

- AWS CodeCommit: AWS-managed Git repository service
- AWS CodeBuild: AWS-managed CI/CD tool that can be used by CodeCommit repositories

AWS account services:

- AWS Identity and Access Management (IAM): used to manage access to an AWS account
- AWS CloudTrail: shows AWS usage history, which is stored in an S3 bucket

There are many more services available.

## Creating an account

First, create an AWS account [here](https://portal.aws.amazon.com/billing/signup).

- This creates a root AWS user
- Make sure to set up 2FA for the root user [here](https://console.aws.amazon.com/iam/home#security_credential)
    - AWS calls it multi-factor authentication (MFA), but you can still use a traditional authenticator app like Duo, Google Authenticator, etc.
    - More details are available [here](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_mfa_enable_virtual.html#enable-virt-mfa-for-root)

Then create an IAM user. It is strongly recommended that you only use your root user account to change account settings (i.e., not everyday use).

- Follow [this](https://docs.aws.amazon.com/singlesignon/latest/userguide/getting-started.html) guide
    - By the end, you should have a IAM user under your root account, and you should only login as that user for most AWS tasks

## Using EC2

The first thing you should try out for free with a new account (under the [AWS Free Tier](https://aws.amazon.com/free/)) is launch an EC2 instance. You can spin up an EC2 instance by following [these instructions](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/EC2_GetStarted.html).

- If you already [generated an SSH key](../ssh#generating-a-ssh-key), you can [import](https://console.aws.amazon.com/ec2#ImportKeyPair) your existing SSH public key to AWS instead

Connect to an EC2 instance via SSH by running

```
ssh <instance-username>@<instance-public-dns-name>
```

- If you didn't import your own SSH key to AWS, add `-i <ssh-private-key>.pem` to the command, specifying the `.pem` file downloaded to your computer
- `<instance-username>` is `ec2-user` for most OSes, `admin` for Debian, and `ubuntu` for Ubuntu
- `<instance-public-dns-name>` can be found by going to the [EC2 Console](https://console.aws.amazon.com/ec2#Instances)
    - It should be located under the "Public IPv4 DNS" column
- You can also use [`scp`](../ssh#scp) and [`sftp`](../ssh#sftp) (both will require the `-i` flag like `ssh` does, if using the AWS-generated key pair)

## Using ECS

Here are helpful videos on how to use ECS to run Docker containers in AWS:

- https://www.youtube.com/watch?v=zs3tyVgiBQQ
- https://www.youtube.com/watch?v=YDNSItBN15w

## Using S3

### Using the AWS Management Console

Follow [this guide](https://docs.aws.amazon.com/AmazonS3/latest/userguide/GetStartedWithS3.html), starting at Step 1 to try out S3 using the AWS Management Console.

### Using the AWS CLI

1. (One-time only) Install the `aws` command using the instructions outlined [here](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
1. Either:
    - (One-time only) Run `aws configure sso` (refer to [these instructions](https://docs.aws.amazon.com/cli/latest/userguide/sso-configure-profile-token.html#sso-configure-profile-token-auto-sso) for help) OR
    - If you have already configured sso but your token has expired, run `aws sso login` to refresh the token

Create a bucket:

```
aws s3 mb s3://<name-of-bucket>
```

- Add `--region us-east-1` if you want to specify a region

Delete a bucket:

```
aws s3 rb s3://<name-of-bucket> --force
```

- `--force` is needed if the bucket is not already empty

List all buckets owned by you:

```
aws s3api list-buckets
```

Copy files and folders:
- Local file to S3:
    ```
    aws s3 cp <file> s3://<name-of-bucket>/<file>
    ```
- S3 file to your local computer:
    ```
    aws s3 cp s3://<name-of-bucket>/<file> .
    ```
- Local folder to S3:
    ```
    aws s3 cp <folder> s3://<name-of-bucket>/<folder>/ --recursive
    ```
- S3 folder to your local computer:
    ```
    aws s3 cp s3://<name-of-bucket>/<folder>/ . --recursive
    ```
- S3 file to S3 folder:
    ```
    aws s3 cp s3://<name-of-bucket>/<file> s3://<name-of-bucket>/<folder>
    ```
- Contents of S3 folder to another S3 folder:
    ```
    aws s3 cp s3://<name-of-bucket>/<folder1>/ s3://<name-of-bucket>/<folder2>/ --recursive
    ```

List all files and folders in a bucket:

```
aws s3 ls s3://<name-of-bucket>
```

Rename files and folders or move files:
- Rename file/folder (also used to move folders):
    ```
    aws s3 mv s3://<name-of-bucket>/old-name s3://<name-of-bucket>/new-name
    ```
- Move file:
    ```
    aws s3 mv s3://<name-of-bucket>/<file> s3://<name-of-bucket>/<folder>/
    ```
- Move contents of one folder to another folder:
    ```
    aws s3 mv s3://<name-of-bucket>/<old-folder>/ s3://<name-of-bucket>/<new-folder>/ --recursive
    ```

Remove files and folders:
- Remove a file:
    ```
    aws s3 rm s3://<name-of-bucket>/<file>
    ```
- Remove a folder:
    ```
    aws s3 rm s3://<name-of-bucket>/<folder> --recursive
    ```

In order to access private S3 buckets from an EC2 instance, you will need to run `aws configure sso` (or `aws sso login` if that was already done) within the EC2 instance to save your AWS credentials.

## Using EFS

[Here](https://aws.amazon.com/getting-started/tutorials/create-network-file-system/?pg=ln&sec=hs) is a tutorial on how to create a file system on AWS and mount it to an EC2 instance running Linux.
