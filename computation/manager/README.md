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
  end
  subgraph :core
    :core:common["common"]
    :core:data["data"]
    :core:model["model"]
    :core:projectstore["projectstore"]
    :core:testing["testing"]
  end
  :computation:manager --> :core:common
  :computation:manager --> :core:data
  :computation:manager --> :core:model
  :computation:manager --> :core:projectstore
  :computation:manager --> :core:testing

classDef android-library fill:#3BD482,stroke:#fff,stroke-width:2px,color:#fff;
classDef unknown fill:#676767,stroke:#fff,stroke-width:2px,color:#fff;
class :computation:manager android-library
class :core:common unknown
class :core:data unknown
class :core:model unknown
class :core:projectstore unknown
class :core:testing unknown

```