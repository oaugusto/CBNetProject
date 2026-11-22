# CBNetProject

A simulator for **self-adjusting networks**: tree networks that rearrange their
own topology in response to the traffic they carry. It implements CBNet
(Counting-Based self-adjusting Network) together with the algorithms it is
compared against (SplayNet, DiSplayNet, a static balanced tree, a static
demand-aware tree) and several later variants.

The simulator is built on [Sinalgo](https://sinalgo.github.io/) 0.75.3, a
round-based simulation framework for distributed algorithms. Sinalgo's source is
vendored under `src/sinalgo`; each algorithm is a Sinalgo *project* under
`src/projects`.

<!-- TODO(Otávio): one or two sentences on the research context and a link to the papers. -->

## Algorithms

Every project reads the same input (a trace of source–destination requests),
builds a binary search tree over the nodes, serves the requests, and logs the
same metrics, so results are directly comparable.

| Project | What it simulates |
|---|---|
| `cbnet` | CBNet. Nodes keep counters (weights); a rotation is performed only when it lowers the rank potential by more than a threshold (`epsilon = -1.5`), otherwise the message is just forwarded. Concurrent requests, Poisson arrivals. |
| `seqcbnet` | CBNet serving one request at a time. |
| `cbnetAdapt` | CBNet with counter decay: every `decayTime` rounds (30, hard-coded) all weights are halved, so old traffic is forgotten. |
| `splaynet` | SplayNet, one request at a time. |
| `displaynet` | DiSplayNet, the distributed and concurrent SplayNet. |
| `semisplaynet` / `seqsemisplaynet` | Semi-splaying variant, concurrent and sequential. <!-- TODO(Otávio): confirm description --> |
| `randomdisplaynet` | Randomised variant: each message is marked "rotation only" with probability `p = 1 / 2^k` (`k` is the `p` parameter). |
| `seqrandomdisplaynet` | The same, one request at a time. |
| `randomdisplaynetAdapt` | Randomised variant with adaptive probability: `p` halves (down to 1/512) each time a node's destination changes and resets to 1 when it repeats. |
| `simplenet` / `seqsimplenet` | Baseline: static balanced binary search tree, routing only, no rotations. |
| `optnet` | Baseline: static demand-aware tree built offline from the trace's communication-frequency matrix. |
| `flatnet`, `flattening` | FlatNet. <!-- TODO(Otávio): describe --> |
| `opticalNet` | Self-adjusting network over an optical switch infrastructure with a central controller. <!-- TODO(Otávio): confirm description --> |
| `defaultProject` | Shared code: tree construction (balanced, linear, random, optimal), the request queue, and data collection. |
| `template` | Sinalgo's empty project template. |

## Requirements

- A JDK (the build targets Java 8 source level)
- [Apache Ant](https://ant.apache.org/)
- Python 3 with `numpy`, only for the batch scripts in `scripts/`

`jdom.jar`, the only runtime dependency, is included in `binaries/`.

## Build

```bash
ant
```

This compiles everything into `binaries/bin` and copies each project's
`Config.xml` next to the classes.

<!-- TODO: not yet verified on a current JDK. -->

## Run

With the GUI, choosing the project in a dialog:

```bash
./sinalgo
```

A single simulation without the GUI:

```bash
./sinalgo -batch -project cbnet -overwrite input=input/trace.txt output=cbnet/trace AutoStart=true
```

Anything after `-overwrite` replaces a value in the project's `Config.xml`:

| Parameter | Meaning | Used by |
|---|---|---|
| `input` | Path to the request trace | all |
| `output` | Folder for the result files | all |
| `mu` | Mean number of rounds between request arrivals (exponential inter-arrival, default 20) | concurrent projects |
| `p` | Exponent `k` of the rotation probability `1 / 2^k` | `randomdisplaynet` |
| `AutoStart` | Start the simulation without waiting for the GUI | all |

The simulation ends when every request in the trace has been completed.

## Input format

A plain text file. The first line is the number of nodes and the number of
requests; every following line is one request, with node ids starting at 0.

```
128,10000
3,97
41,5
...
```

Traces are not part of this repository (`input/` is git-ignored).

<!-- TODO(Otávio): say where the traces can be obtained (Datasets / DataGenerator repos, or the public sources for the Facebook, HPC, pFabric and ProjecToR traces). -->

## Output

Each run writes these files into the `output` folder, inside Sinalgo's `logs/`
directory:

| File | Content |
|---|---|
| `operations.txt` | Summary: sum, mean, standard deviation, min and max of rotations and of routing hops |
| `rotations_per_splay.txt` | Rotations for each request |
| `routing_per_splay.txt` | Routing hops for each request |
| `rounds_per_splay.txt` | Rounds each request took |
| `bypass_per_splay.txt`, `pauses_per_splay.txt` | Bypasses and pauses for each request |
| `total_time.txt` | Total rounds of the simulation |
| `throughput.txt` | Completed requests over time |
| `concurrent_req.txt`, `clusters.txt` | Active requests and clusters per round |
| `sequence.txt` | Order in which requests were started (`src,dst`) |

## Batch experiments

The scripts in `scripts/` run many simulations in parallel threads and record
finished commands in `scripts/logs/`, so an interrupted batch can be resumed.
Run them from the repository root.

```bash
python scripts/sim.py input/trace.txt trace
```

`sim.py` runs one trace through a fixed list of projects. The other scripts
each cover one workload family and expect the traces in a fixed layout under
`input/`; the projects, network sizes and number of repetitions are set by
editing the lists at the top of each script.

| Script | Workload |
|---|---|
| `bursty.py`, `bursty-0.2.py`, `rand-bursty.py` | Bursty traffic |
| `skewed.py`, `rand-skewed.py` | Skewed traffic |
| `normal.py`, `rand-normal.py` | Normally distributed traffic |
| `randomwalk.py` | Random walk |
| `facebook.py` | Facebook datacenter trace |
| `hpc.py` | HPC trace |
| `pfab.py` | pFabric trace |
| `projector.py`, `projector_adapt.py`, `projector_regular.py`, `rand-projector.py` | ProjecToR trace |

Plots for the papers were produced with the R scripts in
[PlotScripts](https://github.com/oaugusto/PlotScripts).

## Repository layout

```
src/sinalgo/      Sinalgo framework (vendored)
src/projects/     One folder per algorithm, plus shared code in defaultProject
scripts/          Batch experiment drivers
binaries/         jdom.jar and other libraries; compiled classes go to binaries/bin
build.xml         Ant build
sinalgo           Launcher (sinalgo.bat on Windows)
```

## Citing

<!-- TODO(Otávio): add the BibTeX entries for the CBNet paper and the related ones. -->

## License

Sinalgo is distributed under a BSD-style licence, copyright ETH Zurich
Distributed Computing Group; see `license.txt`.

<!-- TODO(Otávio): state the licence for the code under src/projects. -->
