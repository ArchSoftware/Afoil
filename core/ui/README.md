### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  subgraph :core
    :core:ui["ui"]
    :core:testing["testing"]
    :core:common["common"]
    :core:designsystem["designsystem"]
    :core:model["model"]
  end
  :core:ui --> :core:testing
  :core:ui --> :core:common
  :core:ui --> :core:designsystem
  :core:ui --> :core:model

classDef android-library fill:#3BD482,stroke:#fff,stroke-width:2px,color:#fff;
classDef unknown fill:#676767,stroke:#fff,stroke-width:2px,color:#fff;
class :core:ui android-library
class :core:testing unknown
class :core:common unknown
class :core:designsystem unknown
class :core:model unknown

```