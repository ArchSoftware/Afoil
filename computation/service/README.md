### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  subgraph :computation
    :computation:service["service"]
    :computation:manager["manager"]
  end
  subgraph :core
    :core:common["common"]
    :core:model["model"]
    :core:notifications["notifications"]
    :core:testing["testing"]
  end
  :computation:service --> :computation:manager
  :computation:service --> :core:common
  :computation:service --> :core:model
  :computation:service --> :core:notifications
  :computation:service --> :core:testing

classDef android-library fill:#3BD482,stroke:#fff,stroke-width:2px,color:#fff;
classDef unknown fill:#676767,stroke:#fff,stroke-width:2px,color:#fff;
class :computation:service android-library
class :computation:manager unknown
class :core:common unknown
class :core:model unknown
class :core:notifications unknown
class :core:testing unknown

```