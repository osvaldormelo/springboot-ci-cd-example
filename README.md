# CI/CD Pipeline with Tekton and ArgoCD for a Spring Boot Application on OpenShift

This repository contains a demonstration of CI/CD for a Spring Boot application using OpenShift GitOps (ArgoCD) and OpenShift Pipelines (Tekton).

## Overview

- **Continuous Integration (CI)**: Implemented using [Openshift Pipelines](https://docs.openshift.com/pipelines/1.15/about/op-release-notes.html), automating the building, testing, and packaging of the Spring Boot application. The pipeline includes cloning the source code, compiling it with Maven, building a Docker image, and pushing it to a container registry.

- **Continuous Deployment (CD)**: Managed by [Openshift GitOps](https://docs.openshift.com/gitops/1.14/understanding_openshift_gitops/about-redhat-openshift-gitops.html), which continuously monitors the Git repository containing Kubernetes manifests and syncs changes to the OpenShift cluster, ensuring automated and reliable deployments.

## Features

- **Automated Builds**: Tekton Pipelines automate the process of building and packaging the application whenever code changes are committed.
- **Containerization**: The application is containerized using Docker, and images are stored in a container registry for deployment.
- **GitOps Workflow**: Both the application code and deployment manifests are version-controlled in Git repositories, promoting transparency and easy rollback if needed.
- **Scalable Deployments**: Utilizing OpenShift's capabilities to manage and scale the application seamlessly.

## Repository Structure

- **demo-app**: Source code of the example Spring Boot application.
- **CI**: YAML files to configure the CI pipeline using OpenShift Pipelines (Tekton).
- **CD**: YAML files to configure CD using OpenShift GitOps (ArgoCD).

## Prerequisites

To run this demo, ensure you have:

1. A configured OpenShift cluster.
2. Administrative access to the cluster to install operators and create namespaces.

## Setup Steps

### 1. Installing the Operators

Install the following operators on OpenShift Hub cluster or in main cluster:

- **OpenShift Pipelines Operator**: To use Tekton.
- **Red Hat OpenShift GitOps Operator**: To use ArgoCD.

You can install these operators via the OpenShift UI or using `oc` commands:

Installing with Openshift UI:

From Openshift Operator Hub Search for Red Hat Openshift GitOps Operator and install it:

![](/images/GitOpsOperator.png)

Install with default options and wait for this screen appears:

![](/images/GitOpsOperator2.png)

Now we need install Openshift Pipelines Operator, back to Operator Hub and search for Red Hat Openshift Pipelines Operator and install it:

![](/images/PipelinesOperator.png)

Again, install with default options and wait for this screen appears:

![](/images/PipelinesOperator2.png)

Installing with `oc` commands:
```sh
# Install OpenShift Pipelines Operator
oc apply -f https://operatorhub.io/path/to/openshift-pipelines-operator.yaml

# Install Red Hat OpenShift GitOps Operator
oc apply -f https://operatorhub.io/path/to/openshift-gitops-operator.yaml
```

### 2. Creating the Namespaces

Create the following namespaces for this project:

- **spring-boot-app-dev**: For the Development environment of Spring Boot application.
- **spring-boot-app-hml**: For the Stage environment of Spring Boot application.
- **spring-boot-app-prd**: For the Production environment of Spring Boot application.
- **pipeline**: For the CI pipeline.

Note: 
In the case of installation in different clusters (one for each environment), the application namespaces must be created in the clusters corresponding to the environments.

Execute this commands:
```sh
oc create namespace spring-boot-app-dev
oc create namespace spring-boot-app-hml
oc create namespace spring-boot-app-prd
oc create namespace pipeline
```

![](/images/Namespaces.png)

### 3. Configuring CI with Tekton


First we need create to secrets, one for image repository(Quay.io in this case) and another to Git commit in infraestructure repository(in this case, for demo purposes only we used the same).

Go to Openshift Console, and access the Workloads session and click in secrets, after, click on create secret:

![](/images/Secret.png)

Select "image pull secret" type and put your access parameters and your secret name 'my-quay-secret' as the follow:

![](/images/Secret2.png)

Now, back to secrets screen, and select create secret again, but now you need select the 'source secret' option. And put your git reposotory data as follow:

![](/images/Secret3.png)

In the `CI` directory, apply the following files to set up the CI pipeline:

1. **WorkspacePvc.yaml**: Creates the Persistent Volume Claim (PVC) for the pipeline workspace.
2. **Task.yaml**: Defines a task used by the pipeline.
3. **Pipeline.yaml**: Defines the CI pipeline.
4. **PipelineRun.yaml**: Runs the defined pipeline.

```sh
oc project pipeline
oc apply -f CI/WorkspacePvc.yaml -n pipeline
oc apply -f CI/TaskCommit.yaml -n pipeline
oc apply -f CI/Pipeline.yaml -n pipeline
oc apply -f CI/PipelineRun.yaml -n pipeline
```

![](/images/PipelineCreated.png)



### 4. Configuring CD with OpenShift GitOps (ArgoCD)

Configure the Continuous Deployment for the Spring Boot application using the files in the `CD` directory:

1. Create an ArgoCD application pointing to this repository.
2. Apply the YAML files in the `CD` directory to define the deployment resources.

Example:

```sh
oc apply -f CD/your-application.yaml -n openshift-gitops
```

### 5. Accessing ArgoCD

After installing the GitOps operator, access the ArgoCD web interface:

1. Get the admin password:

```sh
oc extract secret/openshift-gitops-cluster -n openshift-gitops --to=-
```

2. Access the ArgoCD URL, typically like `https://openshift-gitops-server-openshift-gitops.apps.<cluster-domain>`.

## Final Considerations

- Adjust the namespaces in the YAML files according to your environment.
- Ensure that the operators are installed and operational before applying the manifests.