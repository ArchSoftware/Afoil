### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  subgraph :computation
    :computation:manager["manager"]
    :computation:service["service"]
  end
  subgraph :core
    :core:testing["testing"]
    :core:common["common"]
    :core:data["data"]
    :core:model["model"]
    :core:notifications["notifications"]
    :core:projectstore["projectstore"]
  end
  :core:testing --> :computation:manager
  :core:testing --> :computation:service
  :core:testing --> :core:common
  :core:testing --> :core:data
  :core:testing --> :core:model
  :core:testing --> :core:notifications
  :core:testing --> :core:projectstore

classDef android-library fill:#3BD482,stroke:#fff,stroke-width:2px,color:#fff;
classDef unknown fill:#676767,stroke:#fff,stroke-width:2px,color:#fff;
class :core:testing android-library
class :computation:manager unknown
class :computation:service unknown
class :core:common unknown
class :core:data unknown
class :core:model unknown
class :core:notifications unknown
class :core:projectstore unknown

```